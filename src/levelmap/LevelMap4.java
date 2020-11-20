package levelmap;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import entity.*;
import geom.tile.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import load.*;
import logic.event.*;
import system4.*;

public class LevelMap4 implements XSaveableS
{
	private TileType y1;
	private HashMap<Tile, AdvTile> advTiles;
	private List<XCharacter> allCharacters;
	private List<StartingLocation4> startingLocations;
	private Storage4 storage;
	private int turnCounter;
	private Map<String, EventPack> eventPacks;
	private ArrayList<XArrow> arrows;
	private boolean requiresUpdate;

	public LevelMap4(TileType y1, HashMap<Tile, AdvTile> advTiles,
			List<XCharacter> allCharacters, List<StartingLocation4> startingLocations,
			Storage4 storage, Map<String, EventPack> eventPacks, int turnCounter)
	{
		this.y1 = y1;
		this.advTiles = advTiles;
		this.allCharacters = allCharacters;
		allCharacters.forEach(e -> advTiles.get(e.location()).setEntity(e));
		this.startingLocations = startingLocations;
		this.storage = storage;
		this.eventPacks = eventPacks;
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
			XCharacter entity = advTiles.get(t1).entity();
			/*if(entity != null)
			{
				characters.get(entity.team()).remove(entity);
			}*/
			allCharacters.remove(entity);
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
		/*if(!characters.containsKey(entity.team()))
		{
			characters.put(entity.team(), new ArrayList<>());
		}
		characters.get(entity.team()).add(entity);*/
		allCharacters.add(entity);
		requireUpdate();
	}

	public void removeEntity(XCharacter entity)
	{
		advTile(entity.location()).setEntity(null);
		//characters.get(entity.team()).remove(entity);
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

	public void revertMovement(XCharacter xh)
	{
		xh.resources().revertMovement();
		moveEntity(xh, xh.resources().startLocation());
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
			//StartingLocation startingLocation = startingLocations.get(character.name());
			return true;//startingLocation != null && startingLocation.canSwap();
		}
	}


	public List<XCharacter> allCharacters()
	{
		return allCharacters;
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

	public void setEventPacks(Map<String, EventPack> eventPacks)
	{
		this.eventPacks = eventPacks;
	}

	public List<XArrow> getArrows()
	{
		return arrows;
	}

	public void addArrow(XArrow arrow)
	{
		arrows.add(arrow);
	}

	public void tickArrows()
	{
		arrows.forEach(XArrow::tick);
		arrows.removeIf(XArrow::finished);
	}

	public static LevelMap4 load(JrsObject data, SystemScheme systemScheme)
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
		List<XCharacter> allCharacters = LoadHelper.asList(data.get("Characters"), e -> XCharacter.load(e, y1, systemScheme));
		List<StartingLocation4> startingLocations = LoadHelper.asList(data.get("StartingLocations"), e -> StartingLocation4.load(e, y1));
		Storage4 storage = new Storage4();
		Map<String, EventPack> eventPacks = Map.of();
		int turnCounter = -1;
		return new LevelMap4(y1, advTiles, allCharacters, startingLocations, storage, eventPacks, turnCounter);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
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
				.filter(e -> !e.isSavedInTeam()).collect(Collectors.toList()), a1, y1, systemScheme);
		XSaveableY.saveList("StartingLocations", startingLocations, a1, y1);
	}

	public static LevelMap4 resume(JrsObject data, SystemScheme systemScheme)
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
		List<XCharacter> allCharacters = LoadHelper.asList(data.get("Characters"), e -> XCharacter.load(e, y1, systemScheme));
		List<StartingLocation4> startingLocations = LoadHelper.asList(data.get("StartingLocations"), e -> StartingLocation4.load(e, y1));
		Storage4 storage = Storage4.load((JrsObject) data.get("Storage"), systemScheme);
		int turnCounter = LoadHelper.asInt(data.get("TurnCounter"));
		Map<String, EventPack> eventPacks = Map.of();
		return new LevelMap4(y1, advTiles, allCharacters, startingLocations, storage, eventPacks, turnCounter);
	}

	public void suspend(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
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
		XSaveableYS.saveList("Characters", allCharacters, a1, y1, systemScheme);
		XSaveableY.saveList("StartingLocations", startingLocations, a1, y1);
		XSaveableS.saveObject("Storage", storage, a1, systemScheme);
		a1.put("TurnCounter", turnCounter);
	}

	public void loadTeam(JrsObject data, SystemScheme systemScheme)
	{
		List<XCharacter> teamCharacters = LoadHelper.asList(data.get("Characters"), e -> XCharacter.load(e, y1, systemScheme));
		storage = Storage4.load((JrsObject) data.get("Storage"), systemScheme);
		//TODO place on StartingLocations
	}

	public void saveTeam(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		XSaveableYS.saveList("Characters", allCharacters.stream()
				.filter(XCharacter::isSavedInTeam).collect(Collectors.toList()), a1, y1, systemScheme);
		XSaveableS.saveObject("Storage", storage, a1, systemScheme);
	}
}