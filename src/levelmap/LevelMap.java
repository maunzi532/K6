package levelmap;

import arrow.*;
import building.adv.*;
import building.blueprint.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import entity.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import statsystem.*;
import text.*;

public final class LevelMap implements ConnectRestore, Arrows
{
	public final TileType y1;
	private final HashMap<Tile, AdvTile> advTiles;
	private final List<XBuilding> buildings;
	private final Map<CharacterTeam, List<XCharacter>> characters;
	private final Map<CharSequence, StartingLocation> startingLocations;
	private final Storage storage;
	private int turnCounter;
	private final ArrayList<XArrow> arrows;
	private final ArrayList<Integer> screenshake;
	private boolean requiresUpdate;

	public LevelMap(TileType y1)
	{
		this.y1 = y1;
		advTiles = new HashMap<>();
		buildings = new ArrayList<>();
		characters = new EnumMap<>(CharacterTeam.class);
		startingLocations = new HashMap<>();
		storage = new Storage();
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

	public void productionPhase()
	{
		for(AdvTile advTile : advTiles.values())
		{
			XBuilding building = advTile.building();
			if(building != null)
				building.productionPhase(this, buildingCanWork(building, false));
		}
		for(AdvTile advTile : advTiles.values())
		{
			XBuilding building = advTile.building();
			if(building != null)
				building.afterProduction();
		}
	}

	public void transportPhase()
	{
		for(AdvTile advTile : advTiles.values())
		{
			XBuilding building = advTile.building();
			if(building != null)
				building.transportPhase(this, buildingCanWork(building, false));
		}
		for(AdvTile advTile : advTiles.values())
		{
			XBuilding building = advTile.building();
			if(building != null)
				building.afterTransport();
		}
	}

	private boolean buildingCanWork(XBuilding building, boolean unclaimed)
	{
		return building.costBlueprint().requiredFloorTiles().stream().noneMatch(rft -> y1.range(building.location(), rft.minRange(), rft.maxRange()).stream()
				.filter(e -> okTile(building, e, rft, unclaimed)).count() < rft.amount());
	}

	private boolean okTile(XBuilding building, Tile t1, RequiresFloorTiles rft, boolean unclaimed)
	{
		return getFloor(t1) != null && getFloor(t1).type == rft.floorTileType()
				&& ((unclaimed && getOwner(t1) == null) || getOwner(t1) == building);
	}

	public void toggleTargetClaimed(Tile target, XBuilding building)
	{
		XBuilding owner = getOwner(target);
		if(owner == building)
		{
			removeOwner(target);
			building.removeClaimed(target);
		}
		else if(owner == null)
		{
			addOwner(target, building);
			building.addClaimed(target);
		}
	}

	public void loadConnectBuilding(XBuilding building)
	{
		for(Tile tile : building.claimed())
		{
			addOwner(tile, building);
		}
		building.function().loadConnect(this);
	}

	public void autoClaimFloor(XBuilding building)
	{
		for(RequiresFloorTiles rft : building.costBlueprint().requiredFloorTiles())
		{
			int count = 0;
			for(Tile t1 : y1.range(building.location(), rft.minRange(), rft.maxRange()))
			{
				if(okTile(building, t1, rft, true))
				{
					addOwner(t1, building);
					building.addClaimed(t1);
					count++;
					if(count >= rft.amount())
						break;
				}
			}
		}
	}

	public void buildBuilding(XBuilder builder, CostBlueprint costs, ItemList refundable, BuildingBlueprint blueprint)
	{
		XBuilding building = new XBuilding(builder.location(), costs, refundable, blueprint);
		addBuilding(building);
		autoClaimFloor(building);
		requireUpdate();
	}

	public boolean playerTradeable(XBuilding building, TradeDirection tradeDirection)
	{
		return turnCounter > 0 && !(building.inv(tradeDirection) instanceof BlockedInv);
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

	public boolean canBuild()
	{
		return turnCounter > 0;
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
			XBuilding building = advTiles.get(t1).building();
			if(building != null)
				building.remove();
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

	public XBuilding getBuilding(Tile t1)
	{
		return advTile(t1).building();
	}

	public void addBuilding(XBuilding building)
	{
		advTile(building.location()).setBuilding(building);
		buildings.add(building);
		requireUpdate();
	}

	public XBuilding getOwner(Tile t1)
	{
		return advTile(t1).ownedBy();
	}

	public void addOwner(Tile t1, XBuilding building)
	{
		advTile(t1).setOwnedBy(building);
	}

	public void removeOwner(Tile t1)
	{
		advTile(t1).setOwnedBy(null);
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
				new Pathing(y1, character, character.stats().movement(),
						this, null).start().getEndpoints()
						.stream().flatMap(loc -> attackRanges(character, AttackSide.INITIATOR).stream()
						.flatMap(e -> y1.range(loc, e, e).stream())).distinct())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	public Storage storage()
	{
		return storage;
	}

	public void addStartingLocation(StartingLocation sl)
	{
		startingLocations.put(sl.characterName(), sl);
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

	public static List<Integer> attackRanges(XCharacter entity, AttackSide side)
	{
		List<int[]> v = entity.inv().viewItems(false)
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
	}

	public List<PathAttackX> pathAttackInfo(XCharacter entity, Tile loc, List<XCharacter> possibleTargets, PathLocation pl)
	{
		return possibleTargets.stream().flatMap(e -> attackInfo(entity, loc, e, e.location()).stream())
				.map(e -> new PathAttackX(pl, e)).collect(Collectors.toList());
	}

	public List<AttackInfo> attackInfo(XCharacter entity, XCharacter entityT)
	{
		return attackInfo(entity, entity.location(), entityT, entityT.location());
	}

	public List<AttackInfo> attackInfo(XCharacter entity, Tile loc, XCharacter entityT, Tile locT)
	{
		int distance = y1.distance(loc, locT);
		return entity.inv()
				.viewItems(false)
				.stream()
				.filter(e -> entity.stats().getItemFilter().canContain(e.item))
				.flatMap(e -> ((AttackItem) e.item).attackModes().stream())
				.map(mode -> new AttackInfo(entity, mode, entityT, entityT.stats().lastUsed(), distance))
				.filter(e -> e.canInitiate)
				.map(AttackInfo::addAnalysis)
				.collect(Collectors.toList());
	}

	public void createTile(byte x, byte y, byte sector, byte type)
	{
		advTiles.put(y1.create2(x, y), new AdvTile(new FloorTile(sector, FloorTileType.values()[type])));
	}

	@Override
	public DoubleInv restoreConnection(DoubleInv toConnect)
	{
		if(toConnect instanceof PreConnectMapObject toConnect1)
		{
			return switch(toConnect1.type())
					{
						case BUILDING -> getBuilding(toConnect1.location());
						case ENTITY -> getEntity(toConnect1.location());
					};
		}
		throw new RuntimeException("Connection already restored");
	}

	public String[] saveDataJSON(ItemLoader itemLoader)
	{
		try
		{
			var a1 = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F);
			var h1 = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F);
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
			var a2 = a1.startArrayField("Buildings");
			for(XBuilding building : buildings)
			{
				if(building.active())
					building.save(a2.startObject(), itemLoader, y1);
			}
			a2.end();
			storage.inv().save(h1.startObjectField("Storage"), itemLoader);
			var a3 = a1.startArrayField("Characters");
			var h2 = h1.startArrayField("Characters");
			for(List<XCharacter> c1 : characters.values())
			{
				for(XCharacter character : c1)
				{
					character.save(a3.startObject(), h2, itemLoader, y1);
				}
			}
			a3.end();
			h2.end();
			return new String[]{a1.end().finish(), h1.end().finish()};
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void loadMap(String input, ItemLoader itemLoader)
	{
		try
		{
			TreeNode data = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			if(((JrsNumber) data.get("code")).getValue().intValue() == 0xA4D2839F)
			{
				ByteBuffer sb = ByteBuffer.wrap(Base64.getDecoder().decode(((JrsString) data.get("FloorTiles")).getValue()));
				int lenTiles = sb.remaining() / 4;
				for(int i = 0; i < lenTiles; i++)
				{
					createTile(sb.get(), sb.get(), sb.get(), sb.get());
				}
				((JrsArray) data.get("Buildings")).elements().forEachRemaining(e ->
						addBuilding(new XBuilding((JrsObject) e, itemLoader, y1)));
				((JrsArray) data.get("Characters")).elements().forEachRemaining(e ->
						addEntity(XCharacter.loadFromMap((JrsObject) e, itemLoader, this)));
				((JrsArray) data.get("StartingLocations")).elements().forEachRemaining(e ->
						addStartingLocation(StartingLocation.load((JrsObject) e, itemLoader, y1, startingLocations.size())));
				buildings.forEach(this::loadConnectBuilding);
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public String saveMap(ItemLoader itemLoader)
	{
		try
		{
			var a1 = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F);
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
			var a2 = a1.startArrayField("Buildings");
			for(XBuilding building : buildings)
			{
				if(building.active())
					building.save(a2.startObject(), itemLoader, y1);
			}
			a2.end();
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
			return a1.end().finish();
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void loadTeam(String input, ItemLoader itemLoader)
	{
		try
		{
			TreeNode data = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			if(((JrsNumber) data.get("code")).getValue().intValue() == 0xA4D2839F)
			{
				//TODO ((JrsValue) data.get("CurrentMap")).asText();
				WeightInv tempInv = new WeightInv((JrsObject) data.get("Storage"), itemLoader);
				storage.inv().tryAdd(tempInv.allItems());
				((JrsArray) data.get("Team")).elements().forEachRemaining(e ->
						{
							JrsObject e1 = (JrsObject) e;
							StartingLocation sl = startingLocations.get(new NameText(((JrsObject) e1.get("Stats")).get("CustomName").asText()));
							addEntity(XCharacter.loadFromTeam(e1, sl.startingDelay(), sl.location(), sl.invOverride(), storage.inv(), itemLoader, this));
						});
				buildings.forEach(this::loadConnectBuilding);
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public String saveTeamStart(String mapFile, ItemLoader itemLoader)
	{
		try
		{
			var a1 = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F);
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
			storage.inv().save(a1.startObjectField("Storage"), itemLoader);
			return a1.end().finish();
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}