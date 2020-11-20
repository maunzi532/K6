package levelmap;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import entity.*;
import geom.tile.*;
import item.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import logic.event.*;
import text.*;

public final class LevelMap implements Arrows
{
	public final TileType y1;
	private final HashMap<Tile, AdvTile> advTiles;
	private final Map<CharacterTeam, List<XCharacter>> characters;
	private final Map<CharSequence, StartingLocation> startingLocations;
	private final Storage4 storage;
	private Map<String, EventPack> eventPacks;
	private int turnCounter;
	private final ArrayList<XArrow> arrows;
	private final ArrayList<Integer> screenshake;
	private boolean requiresUpdate;

	public LevelMap(TileType y1)
	{
		this.y1 = y1;
		advTiles = new HashMap<>();
		characters = new EnumMap<>(CharacterTeam.class);
		startingLocations = new HashMap<>();
		storage = new Storage4();
		turnCounter = -1;
		arrows = new ArrayList<>();
		screenshake = new ArrayList<>();
	}

	public AdvTile advTile(Tile t1)
	{
		return advTiles.getOrDefault(t1, AdvTile.EMPTY);
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

	public boolean playerTradeable(XCharacter character)
	{
		if(turnCounter == 0)
		{
			StartingLocation startingLocation = startingLocations.get(character.name());
			return startingLocation != null && startingLocation.canTrade();
		}
		else
		{
			return false;
		}
	}

	public boolean playerTradeableStorage()
	{
		return turnCounter == 0;
	}

	public boolean canSwap(XCharacter character)
	{
		if(turnCounter == 0)
		{
			StartingLocation startingLocation = startingLocations.get(character.name());
			return startingLocation != null && startingLocation.canSwap();
		}
		else
		{
			return false;
		}
	}

	public void revertMovement(XCharacter xh)
	{
		xh.resources().revertMovement();
		moveEntity(xh, xh.resources().startLocation());
		requireUpdate();
	}

	public void clearTile(Tile t1)
	{
		if(advTiles.containsKey(t1))
		{
			XCharacter entity = advTiles.get(t1).entity();
			if(entity != null)
				characters.get(entity.team()).remove(entity);
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
		if(!characters.containsKey(entity.team()))
		{
			characters.put(entity.team(), new ArrayList<>());
		}
		characters.get(entity.team()).add(entity);
		/*//TODO
		if(entity.team() == CharacterTeam.HERO)
		{
			startingLocations.put(entity.name(), new StartingLocation(startingLocations.size(), entity.name(),
					entity.location(), true, null, 0));
		}*/
		requireUpdate();
	}

	public void removeEntity(XCharacter entity)
	{
		advTile(entity.location()).setEntity(null);
		characters.get(entity.team()).remove(entity);
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

	public Optional<XCharacter> findByName(CharSequence name)
	{
		if(name == null)
			return Optional.empty();
		else
			return characters.entrySet().stream().flatMap(e -> e.getValue().stream().filter(e1 -> e1.name().equals(name))).findFirst();
	}

	public List<XCharacter> teamCharacters(CharacterTeam team)
	{
		return characters.getOrDefault(team, List.of());
	}

	public List<XCharacter> teamTargetCharacters(CharacterTeam team)
	{
		return characters.getOrDefault(team, List.of()).stream().filter(XCharacter::targetable).collect(Collectors.toList());
	}

	public List<XCharacter> enemyTeamTargetCharacters(CharacterTeam team)
	{
		return characters.entrySet().stream().filter(e -> e.getKey() != team).flatMap(e -> e.getValue().stream())
				.filter(XCharacter::targetable).collect(Collectors.toList());
	}

	public Map<Tile, Long> allEnemyReach()
	{
		return teamTargetCharacters(CharacterTeam.ENEMY).stream().flatMap(character ->
				new Pathing(y1, character, character.movement(),
						/*this*/null, null).start().getEndpoints()
						.stream().flatMap(loc -> character.attackRanges().stream()
						.flatMap(e -> y1.range(loc, e, e).stream())).distinct())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	public Storage4 storage()
	{
		return storage;
	}

	public void addStartingLocation(StartingLocation sl)
	{
		startingLocations.put(sl.characterName(), sl);
	}

	public void setEventPacks(Map<String, EventPack> eventPacks)
	{
		this.eventPacks = eventPacks;
	}

	public EventPack eventPack(String key)
	{
		return eventPacks.get(key);
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

	public List<XArrow> getArrows()
	{
		return arrows;
	}

	@Override
	public void addArrow(XArrow arrow)
	{
		arrows.add(arrow);
	}

	@Override
	public void addScreenshake(int power)
	{
		screenshake.add(power);
	}

	public int removeFirstScreenshake()
	{
		if(screenshake.isEmpty())
			return 0;
		else
			return screenshake.remove(0);
	}

	public void tickArrows()
	{
		arrows.forEach(XArrow::tick);
		arrows.removeIf(XArrow::finished);
	}

	/*public static List<Integer> attackRanges(XCharacter entity, AttackSide side)
	{
		List<int[]> v = entity.inv().viewItems()
				.stream().filter(e -> entity.stats().getItemFilter().canContain(e.item))
				.map(e -> ((AttackItem) e.item).getRanges(side)).collect(Collectors.toList());
		Set<Integer> ints2 = new HashSet<>();
		for(int[] ints : v)
		{
			for(int n : ints)
			{
				ints2.add(n);
			}
		}
		return ints2.stream().sorted().collect(Collectors.toList());
	}*/

	/*public List<PathAttackX> pathAttackInfo(XCharacter entity, Tile loc, List<XCharacter> possibleTargets, PathLocation pl)
	{
		return possibleTargets.stream().flatMap(e -> attackInfo(entity, loc, e, e.location()).stream())
				.map(e -> new PathAttackX(pl, e)).collect(Collectors.toList());
	}

	public List<AttackInfo> attackInfo(XCharacter entity, XCharacter entityT)
	{
		return attackInfo(entity, entity.location(), entityT, entityT.location());
	}*/

	/*public List<AttackInfo> attackInfo(XCharacter entity, Tile loc, XCharacter entityT, Tile locT)
	{
		int distance = y1.distance(loc, locT);
		return entity.inv()
				.viewItems()
				.stream()
				.filter(e -> entity.stats().getItemFilter().canContain(e.item))
				.flatMap(e -> ((AttackItem) e.item).attackModes().stream())
				.map(mode -> new AttackInfo(entity, mode, entityT, entityT.stats().lastUsed(), distance))
				.filter(e -> e.canInitiate)
				.map(AttackInfo::addAnalysis)
				.collect(Collectors.toList());
	}*/

	public void createTile(byte x, byte y, byte sector, byte type)
	{
		advTiles.put(y1.create2(x, y), new AdvTile(new FloorTile(sector, FloorTileType.values()[type])));
	}

	public void loadMap(JrsObject data, ItemLoader itemLoader)
	{
		ByteBuffer sb = ByteBuffer.wrap(Base64.getDecoder().decode(((JrsString) data.get("FloorTiles")).getValue()));
		int lenTiles = sb.remaining() / 4;
		for(int i = 0; i < lenTiles; i++)
		{
			createTile(sb.get(), sb.get(), sb.get(), sb.get());
		}
		((JrsArray) data.get("Characters")).elements().forEachRemaining(e ->
				addEntity(XCharacter.loadFromMap((JrsObject) e, itemLoader, this)));
		((JrsArray) data.get("StartingLocations")).elements().forEachRemaining(e ->
				addStartingLocation(StartingLocation.load((JrsObject) e, itemLoader, y1, startingLocations.size())));
	}

	public <T extends ComposerBase> void saveMapNC(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
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
		var a3 = a1.startArrayField("Characters");
		for(List<XCharacter> c1 : characters.values())
		{
			for(XCharacter character : c1)
			{
				if(!startingLocations.containsKey(character.name()))
					character.saveToMap(a3.startObject(), itemLoader, y1);
			}
		}
		a3.end();
		var a4 = a1.startArrayField("StartingLocations");
		List<StartingLocation> startingLocations1 = startingLocations.values().stream()
				.sorted(Comparator.comparingInt(StartingLocation::number)).collect(Collectors.toList());
		for(StartingLocation sl : startingLocations1)
		{
			sl.save(a4.startObject(), itemLoader, y1);
		}
		a4.end();
	}

	public void loadTeam(JrsObject data, ItemLoader itemLoader)
	{
		/*WeightInv tempInv = new WeightInv((JrsObject) data.get("Storage"), itemLoader);
		storage.inv().tryAdd(tempInv.allItems());*/
		((JrsArray) data.get("Team")).elements().forEachRemaining(e ->
				{
					JrsObject e1 = (JrsObject) e;
					StartingLocation sl = startingLocations.get(new NameText(((JrsObject) e1.get("Stats")).get("CustomName").asText()));
					addEntity(XCharacter.loadFromTeam(e1, sl.startingDelay(), sl.location(), sl.invOverride(), storage.inv(), itemLoader, this));
				});
	}

	public <T extends ComposerBase> void saveTeamStartNC(ObjectComposer<T> a1, String worldFolder, String mapFile, ItemLoader itemLoader) throws IOException
	{
		a1.put("World", worldFolder);
		a1.put("CurrentMap", mapFile);
		var a2 = a1.startArrayField("Team");
		for(List<XCharacter> c1 : characters.values())
		{
			for(XCharacter character : c1)
			{
				StartingLocation sl = startingLocations.get(character.name());
				if(sl != null)
					character.saveToTeam(a2.startObject(), true, sl.canTrade(), itemLoader, y1);
			}
		}
		a2.end();
		//storage.inv().save(a1.startObjectField("Storage"), itemLoader);
	}
}