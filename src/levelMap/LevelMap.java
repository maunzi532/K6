package levelMap;

import arrow.*;
import building.adv.*;
import building.blueprint.*;
import com.fasterxml.jackson.jr.ob.*;
import doubleinv.*;
import entity.*;
import geom.f1.*;
import item.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import system2.*;

public class LevelMap implements ConnectRestore, Arrows
{
	public final TileType y1;
	private final HashMap<Tile, AdvTile> advTiles;
	private final List<Boolean> visibleSectors;
	private final ArrayList<XBuilding> buildings;
	private final HashMap<CharacterTeam, List<XCharacter>> characters;
	private final ArrayList<XArrow> arrows;
	private final ArrayList<Integer> screenshake;

	public LevelMap(TileType y1)
	{
		this.y1 = y1;
		advTiles = new HashMap<>();
		visibleSectors = new ArrayList<>();
		buildings = new ArrayList<>();
		characters = new HashMap<>();
		arrows = new ArrayList<>();
		screenshake = new ArrayList<>();
	}

	public AdvTile advTile(Tile t1)
	{
		return advTiles.getOrDefault(t1, AdvTile.EMPTY);
	}

	public void productionPhase()
	{
		for(AdvTile advTile : advTiles.values())
		{
			XBuilding building = advTile.getBuilding();
			if(building != null)
				building.productionPhase(this, buildingCanWork(building, false));
		}
		for(AdvTile advTile : advTiles.values())
		{
			XBuilding building = advTile.getBuilding();
			if(building != null)
				building.afterProduction();
		}
	}

	public void transportPhase()
	{
		for(AdvTile advTile : advTiles.values())
		{
			XBuilding building = advTile.getBuilding();
			if(building != null)
				building.transportPhase(this, buildingCanWork(building, false));
		}
		for(AdvTile advTile : advTiles.values())
		{
			XBuilding building = advTile.getBuilding();
			if(building != null)
				building.afterTransport();
		}
	}

	public boolean buildingCanWork(XBuilding building, boolean unclaimed)
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
		building.function().loadConnect(this, building);
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
	}

	private void charactersAdd(XCharacter character)
	{
		if(!characters.containsKey(character.team()))
		{
			characters.put(character.team(), new ArrayList<>());
		}
		characters.get(character.team()).add(character);
	}

	public void revertMovement(XCharacter xh)
	{
		xh.resources().revertMovement();
		moveEntity(xh, xh.resources().startLocation());
	}

	public void clearTile(Tile t1)
	{
		if(advTiles.containsKey(t1))
		{
			XCharacter entity = advTiles.get(t1).getEntity();
			charactersAdd(entity);
			if(advTiles.get(t1).getBuilding() != null)
				advTiles.get(t1).getBuilding().remove();
			advTiles.remove(t1);
		}
	}

	public FloorTile getFloor(Tile t1)
	{
		return advTile(t1).getFloorTile();
	}

	public void setFloorTile(Tile t1, FloorTile floorTile)
	{
		while(floorTile.sector >= visibleSectors.size())
			visibleSectors.add(true);
		if(advTiles.containsKey(t1))
		{
			advTile(t1).setFloorTile(floorTile);
		}
		else
			advTiles.put(t1, new AdvTile(floorTile));
	}

	public XBuilding getBuilding(Tile t1)
	{
		return advTile(t1).getBuilding();
	}

	public void addBuilding(XBuilding building)
	{
		advTile(building.location()).setBuilding(building);
		buildings.add(building);
	}

	public XBuilding getOwner(Tile t1)
	{
		return advTile(t1).getOwned();
	}

	public void addOwner(Tile t1, XBuilding building)
	{
		advTile(t1).setOwned(building);
	}

	public void removeOwner(Tile t1)
	{
		advTile(t1).setOwned(null);
	}

	public XCharacter getEntity(Tile t1)
	{
		return advTile(t1).getEntity();
	}

	public void addEntity(XCharacter entity)
	{
		advTile(entity.location()).setEntity(entity);
		charactersAdd(entity);
	}

	public void removeEntity(XCharacter entity)
	{
		advTile(entity.location()).setEntity(null);
		characters.get(entity.team()).remove(entity);
	}

	public void moveEntity(XCharacter entity, Tile newLocation)
	{
		XArrow arrow = XArrow.factory(entity.location(), newLocation, y1, false, /*entity.getImage()*/XCharacter.IMAGE); //TODO
		addArrow(arrow);
		entity.replaceVisual(arrow);
		advTile(entity.location()).setEntity(null);
		entity.setLocation(newLocation);
		advTile(newLocation).setEntity(entity);
	}

	public void swapEntities(XCharacter entity1, XCharacter entity2)
	{
		Tile location1 = entity1.location();
		Tile location2 = entity2.location();
		XArrow arrow1 = XArrow.factory(location1, location2, y1, false, /*entity.getImage()*/XCharacter.IMAGE);
		addArrow(arrow1);
		entity1.replaceVisual(arrow1);
		XArrow arrow2 = XArrow.factory(location2, location1, y1, false, /*entity.getImage()*/XCharacter.IMAGE);
		addArrow(arrow2);
		entity2.replaceVisual(arrow2);
		advTile(location1).setEntity(entity2);
		advTile(location2).setEntity(entity1);
		entity1.setLocation(location2);
		entity2.setLocation(location1);
	}

	public List<XCharacter> teamCharacters(CharacterTeam team)
	{
		return characters.getOrDefault(team, List.of());
	}

	public int createSector(boolean visible)
	{
		visibleSectors.add(visible);
		return visibleSectors.size() - 1;
	}

	public boolean sectorVisible(int sector)
	{
		return visibleSectors.get(sector);
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

	public List<Integer> attackRanges(XCharacter entity, boolean counter)
	{
		List<int[]> v = entity.outputInv().viewItems(false)
				.stream().filter(e -> entity.stats().getItemFilter().canContain(e.item))
				.map(e -> ((AttackItem2) e.item).getRanges(counter)).collect(Collectors.toList());
		HashSet<Integer> ints2 = new HashSet<>();
		for(int[] ints : v)
		{
			for(int i = 0; i < ints.length; i++)
			{
				ints2.add(ints[i]);
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
		return entity.outputInv()
				.viewItems(false)
				.stream()
				.filter(e -> entity.stats().getItemFilter().canContain(e.item))
				.flatMap(e -> ((AttackItem2) e.item).attackModes().stream())
				.map(mode -> new AttackInfo(entity, loc, mode, entityT, locT, entityT.stats().lastUsed(), distance))
				.filter(e -> e.canInitiate)
				.map(AttackInfo::addAnalysis)
				.collect(Collectors.toList());
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
		throw new RuntimeException();
	}

	public String[] saveDataJSON(ItemLoader itemLoader)
	{
		try
		{
			var a1 = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F);
			var xheroSave = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F)
					.startArrayField("Characters");
			ByteBuffer sb = ByteBuffer.allocate(advTiles.size() * 4);
			for(Map.Entry<Tile, AdvTile> entry : advTiles.entrySet())
			{
				Tile t1 = entry.getKey();
				AdvTile adv = entry.getValue();
				if(adv.getFloorTile() != null)
				{
					sb.put((byte) y1.sx(t1));
					sb.put((byte) y1.sy(t1));
					sb.put((byte) adv.getFloorTile().sector);
					sb.put((byte) adv.getFloorTile().type.ordinal());
				}
			}
			var a2 = a1.put("FloorTiles", Base64.getEncoder().encodeToString(sb.array()))
					.startArrayField("Buildings");
			for(XBuilding building : buildings)
			{
				if(building.active())
					a2 = building.save(a2.startObject(), itemLoader, y1).end();
			}
			var a3 = a2.end().startArrayField("XEntities");
			for(List<XCharacter> c1 : characters.values())
			{
				for(XCharacter character : c1)
				{
					a3 = character.save(a3.startObject(), itemLoader, y1).end();
					if(character.saveSettings() != null)
						xheroSave = character.save3(xheroSave.startObject(), itemLoader).end();
				}
			}
			return new String[]{a3.end().end().finish(), xheroSave.end().end().finish()};
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void createTile(byte x, byte y, byte s, byte t)
	{
		while(s >= visibleSectors.size())
			visibleSectors.add(true);
		advTiles.put(y1.create2(x, y), new AdvTile(new FloorTile(s, FloorTileType.values()[t])));
	}
}