package levelmap;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import geom.tile.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import load.*;
import system.*;

public class LevelMap implements Arrows, XSaveableS
{
	private TileType y1;
	private HashMap<Tile, AdvTile> advTiles;
	private List<XCharacter> allCharacters;
	private List<StartingLocation> allStartingLocations;
	private Storage storage;
	private int turnCounter;
	private boolean win;
	private boolean lose;
	private Map<String, ? extends XEventPack> eventPacks;
	private String nextLevel;
	private ArrayList<XArrow> arrows;
	private boolean requiresUpdate;

	public LevelMap(TileType y1, HashMap<Tile, AdvTile> advTiles,
			List<XCharacter> allCharacters, List<StartingLocation> allStartingLocations,
			Storage storage, Map<String, ? extends XEventPack> eventPacks, String nextLevel, int turnCounter)
	{
		this.y1 = y1;
		this.advTiles = advTiles;
		this.allCharacters = allCharacters;
		allCharacters.forEach(e -> advTiles.get(e.location()).setEntity(e));
		this.allStartingLocations = allStartingLocations;
		allStartingLocations.forEach(e -> advTiles.get(e.location()).setStartingLocation(e));
		this.storage = storage;
		this.eventPacks = eventPacks;
		this.nextLevel = nextLevel;
		this.turnCounter = turnCounter;
		arrows = new ArrayList<>();
	}

	public boolean checkUpdate()
	{
		boolean r = requiresUpdate;
		requiresUpdate = false;
		return r;
	}

	public void requireUpdate()
	{
		requiresUpdate = true;
	}

	public TileType y1()
	{
		return y1;
	}

	public AdvTile advTile(Tile t1)
	{
		return advTiles.getOrDefault(t1, AdvTile.EMPTY);
	}

	public void clearTile(Tile t1)
	{
		if(advTiles.containsKey(t1))
		{
			allCharacters.remove(advTiles.get(t1).entity());
			allStartingLocations.remove(advTiles.get(t1).startingLocation());
			advTiles.remove(t1);
			requireUpdate();
		}
	}

	public FloorTile getFloor(Tile t1)
	{
		return advTile(t1).floorTile();
	}

	public void setFloorTile(Tile t1, FloorTile floorTile)
	{
		if(advTiles.containsKey(t1))
		{
			advTile(t1).setFloorTile(floorTile);
		}
		else
			advTiles.put(t1, new AdvTile(floorTile));
		requireUpdate();
	}

	public XCharacter getEntity(Tile t1)
	{
		return advTile(t1).entity();
	}

	public void addEntity(XCharacter entity)
	{
		advTile(entity.location()).setEntity(entity);
		allCharacters.add(entity);
		requireUpdate();
	}

	public void removeEntity(XCharacter entity)
	{
		advTile(entity.location()).setEntity(null);
		allCharacters.remove(entity);
		requireUpdate();
	}

	public void moveEntity(XCharacter entity, Tile newLocation)
	{
		XArrow arrow = XArrow.factory(entity.location(), newLocation, y1, false, entity.mapImageName());
		addArrow(arrow);
		entity.replaceVisual(arrow);
		advTile(entity.location()).setEntity(null);
		entity.setLocation(newLocation);
		advTile(newLocation).setEntity(entity);
		requireUpdate();
	}

	public void swapEntities(XCharacter entity1, XCharacter entity2)
	{
		Tile location1 = entity1.location();
		Tile location2 = entity2.location();
		XArrow arrow1 = XArrow.factory(location1, location2, y1, false, entity1.mapImageName());
		addArrow(arrow1);
		entity1.replaceVisual(arrow1);
		XArrow arrow2 = XArrow.factory(location2, location1, y1, false, entity2.mapImageName());
		addArrow(arrow2);
		entity2.replaceVisual(arrow2);
		advTile(location1).setEntity(entity2);
		advTile(location2).setEntity(entity1);
		entity1.setLocation(location2);
		entity2.setLocation(location1);
		requireUpdate();
	}

	public boolean canSwap(XCharacter character)
	{
		if(levelStarted())
		{
			return false;
		}
		else
		{
			StartingLocation startingLocation = advTile(character.location()).startingLocation();
			return startingLocation != null && !startingLocation.locationLocked();
		}
	}

	public boolean playerTradeable(XCharacter character)
	{
		if(levelStarted())
		{
			return true;
		}
		else
		{
			StartingLocation startingLocation = advTile(character.location()).startingLocation();
			return startingLocation != null && !startingLocation.emptyInv();
		}
	}

	public List<XCharacter> allCharacters()
	{
		return allCharacters;
	}

	public Map<Tile, Long> allEnemyReach()
	{
		return allCharacters.stream().filter(e -> e.team() == CharacterTeam.ENEMY && e.targetable()).flatMap(character ->
				new Pathing(character, character.movement(), this, true).getEndpoints()
						.stream().flatMap(loc -> character.enemyTargetRanges(true).stream()
						.flatMap(e -> y1.range(loc, e, e).stream())))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	public void setDefeated(XCharacter character)
	{
		if(character.isSavedInTeam())
		{
			lose = true;
		}
		else
		{
			removeEntity(character);
			if(allCharacters.stream().noneMatch(e -> e.team() == CharacterTeam.HERO))
				lose = true;
			if(allCharacters.stream().noneMatch(e -> e.team() == CharacterTeam.ENEMY))
				win = true;
		}
	}

	public boolean isWin()
	{
		return win;
	}

	public boolean isLose()
	{
		return lose;
	}

	public Storage storage()
	{
		return storage;
	}

	public boolean levelStarted()
	{
		return turnCounter > 0;
	}

	public int turnCounter()
	{
		return turnCounter;
	}

	public void increaseTurnCounter()
	{
		turnCounter++;
	}

	public void setEventPacks(Map<String, ? extends XEventPack> eventPacks)
	{
		this.eventPacks = eventPacks;
	}

	public String nextLevel()
	{
		return nextLevel;
	}

	@Override
	public void addScreenshake(int power)
	{
		//TODO
	}

	public List<XArrow> getArrows()
	{
		return arrows;
	}

	@Override
	public void addArrow(XArrow arrow)
	{
		arrows.add(arrow);
	}

	public void tickArrows()
	{
		arrows.forEach(XArrow::tick);
		arrows.removeIf(XArrow::finished);
	}

	public static LevelMap load(JrsObject data, WorldSettings worldSettings)
	{
		TileType y1 = switch(data.get("TileType").asText())
				{
					case "Hex" -> new HexTileType();
					case "Quad" -> new QuadTileType();
					default -> throw new RuntimeException("Wrong TileType");
				};
		HashMap<Tile, AdvTile> advTiles = new HashMap<>();
		ByteBuffer sb = ByteBuffer.wrap(Base64.getDecoder().decode(((JrsString) data.get("FloorTiles")).getValue()));
		while(sb.remaining() > 0)
		{
			advTiles.put(y1.create2(sb.get(), sb.get()), new AdvTile(new FloorTile(sb.get(), FloorTileType.values()[sb.get()])));
		}
		List<XCharacter> allCharacters = LoadHelper.asList(data.get("Characters"), e -> XCharacter.load(e, y1,
				worldSettings));
		List<StartingLocation> startingLocations = LoadHelper.asList(data.get("StartingLocations"), e -> StartingLocation
				.load(e, y1));
		Storage storage = new Storage();
		Map<String, XEventPack> eventPacks = Map.of();
		String nextLevel = LoadHelper.asOptionalString(data.get("NextLevel"));
		int turnCounter = 0;
		return new LevelMap(y1, advTiles, allCharacters, startingLocations, storage, eventPacks, nextLevel, turnCounter);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		if(y1 instanceof HexTileType)
			a1.put("TileType", "Hex");
		else if(y1 instanceof QuadTileType)
			a1.put("TileType", "Quad");
		ByteBuffer sb = ByteBuffer.allocate(advTiles.size() * 4);
		for(Map.Entry<Tile, AdvTile> entry : advTiles.entrySet())
		{
			Tile t1 = entry.getKey();
			AdvTile adv = entry.getValue();
			if(adv.floorTile() != null)
			{
				sb.put((byte) y1.sx(t1));
				sb.put((byte) y1.sy(t1));
				sb.put((byte) adv.floorTile().sector);
				sb.put((byte) adv.floorTile().type.ordinal());
			}
		}
		a1.put("FloorTiles", Base64.getEncoder().encodeToString(sb.array()));
		XSaveableYS.saveList("Characters", allCharacters.stream()
				.filter(e -> !e.isSavedInTeam()).collect(Collectors.toList()), a1, y1, worldSettings);
		XSaveableY.saveList("StartingLocations", allStartingLocations, a1, y1);
		if(nextLevel != null)
			a1.put("NextLevel", nextLevel);
	}

	public static LevelMap resume(JrsObject data, WorldSettings worldSettings)
	{
		TileType y1 = switch(data.get("TileType").asText())
				{
					case "Hex" -> new HexTileType();
					case "Quad" -> new QuadTileType();
					default -> throw new RuntimeException("Wrong TileType");
				};
		HashMap<Tile, AdvTile> advTiles = new HashMap<>();
		ByteBuffer sb = ByteBuffer.wrap(Base64.getDecoder().decode(data.get("FloorTiles").asText()));
		while(sb.remaining() > 0)
		{
			advTiles.put(y1.create2(sb.get(), sb.get()), new AdvTile(new FloorTile(sb.get(), FloorTileType.values()[sb.get()])));
		}
		List<XCharacter> allCharacters = LoadHelper.asList(data.get("Characters"), e -> XCharacter.load(e, y1,
				worldSettings));
		List<StartingLocation> startingLocations = LoadHelper.asList(data.get("StartingLocations"), e -> StartingLocation
				.load(e, y1));
		Storage storage = Storage.load((JrsObject) data.get("Storage"), worldSettings);
		int turnCounter = LoadHelper.asInt(data.get("TurnCounter"));
		Map<String, XEventPack> eventPacks = Map.of();
		String nextLevel = LoadHelper.asOptionalString(data.get("NextLevel"));
		return new LevelMap(y1, advTiles, allCharacters, startingLocations, storage, eventPacks, nextLevel, turnCounter);
	}

	public void suspend(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		if(y1 instanceof HexTileType)
			a1.put("TileType", "Hex");
		else if(y1 instanceof QuadTileType)
			a1.put("TileType", "Quad");
		ByteBuffer sb = ByteBuffer.allocate(advTiles.size() * 4);
		for(Map.Entry<Tile, AdvTile> entry : advTiles.entrySet())
		{
			Tile t1 = entry.getKey();
			AdvTile adv = entry.getValue();
			if(adv.floorTile() != null)
			{
				sb.put((byte) y1.sx(t1));
				sb.put((byte) y1.sy(t1));
				sb.put((byte) adv.floorTile().sector);
				sb.put((byte) adv.floorTile().type.ordinal());
			}
		}
		a1.put("FloorTiles", Base64.getEncoder().encodeToString(sb.array()));
		XSaveableYS.saveList("Characters", allCharacters, a1, y1, worldSettings);
		XSaveableY.saveList("StartingLocations", allStartingLocations, a1, y1);
		XSaveableS.saveObject("Storage", storage, a1, worldSettings);
		a1.put("TurnCounter", turnCounter);
		if(nextLevel != null)
			a1.put("NextLevel", nextLevel);
	}

	public void loadTeam(JrsObject data, WorldSettings worldSettings)
	{
		List<XCharacter> teamCharacters = LoadHelper.asList(data.get("Characters"), e -> XCharacter.load(e, y1,
				worldSettings));
		storage = Storage.load((JrsObject) data.get("Storage"), worldSettings);
		List<StartingLocation> namedSL = allStartingLocations.stream().filter(e -> e.startName() != null).collect(Collectors.toList());
		ArrayList<XCharacter> namedC = new ArrayList<>();
		namedSL.forEach(e -> namedC.add(setToStartingLocation(characterByName(teamCharacters, e.startName()), e)));
		List<StartingLocation> unnamedSL = allStartingLocations.stream().filter(e -> e.startName() == null).collect(Collectors.toList());
		List<XCharacter> unnamedC = teamCharacters.stream().filter(e -> !namedC.contains(e)).collect(Collectors.toList());
		for(int i = 0; i < unnamedC.size() && i < unnamedSL.size(); i++)
		{
			setToStartingLocation(unnamedC.get(i), unnamedSL.get(i));
		}
	}

	private static XCharacter characterByName(List<XCharacter> characters, String name)
	{
		return characters.stream().filter(e1 -> e1.isNamed(name)).findFirst().orElseThrow();
	}

	private XCharacter setToStartingLocation(XCharacter character, StartingLocation startingLocation)
	{
		advTiles.get(startingLocation.location()).setEntity(character);
		allCharacters.add(character);
		//TODO lock characters with locked movement/inv
		if(startingLocation.emptyInv())
		{
			//TODO add inv contents into storage
		}
		return character;
	}

	public void saveTeam(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		XSaveableYS.saveList("Characters", allCharacters.stream()
				.filter(XCharacter::isSavedInTeam).collect(Collectors.toList()), a1, y1, worldSettings);
		XSaveableS.saveObject("Storage", storage, a1, worldSettings);
	}
}