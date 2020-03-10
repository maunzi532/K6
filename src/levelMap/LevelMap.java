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

public class LevelMap implements ConnectRestore, Arrows
{
	public final TileType y1;
	private final HashMap<Tile, AdvTile> advTiles;
	private final List<Boolean> visibleSectors;
	private final ArrayList<XBuilding> buildings;
	private final ArrayList<XHero> entitiesH;
	private final ArrayList<XEnemy> entitiesE;
	private final ArrayList<XEntity> entities3;
	private final ArrayList<XArrow> arrows;
	private final ArrayList<Integer> screenshake;

	public LevelMap(TileType y1)
	{
		this.y1 = y1;
		advTiles = new HashMap<>();
		visibleSectors = new ArrayList<>();
		buildings = new ArrayList<>();
		entitiesH = new ArrayList<>();
		entitiesE = new ArrayList<>();
		entities3 = new ArrayList<>();
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

	public void revertMovement(XHero xh)
	{
		moveEntity(xh, xh.getRevertLocation());
		xh.reactivateMovement();
	}

	public void clearTile(Tile t1)
	{
		if(advTiles.containsKey(t1))
		{
			XEntity entity = advTiles.get(t1).getEntity();
			if(entity instanceof XHero)
				entitiesH.remove(entity);
			else if(entity instanceof XEnemy)
				entitiesE.remove(entity);
			else
				entities3.remove(entity);
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

	public XEntity getEntity(Tile t1)
	{
		return advTile(t1).getEntity();
	}

	public void addEntity(XEntity entity)
	{
		advTile(entity.location()).setEntity(entity);
		if(entity instanceof XHero)
			entitiesH.add((XHero) entity);
		else if(entity instanceof XEnemy)
			entitiesE.add((XEnemy) entity);
		else
			entities3.add(entity);
	}

	public void removeEntity(XEntity entity)
	{
		advTile(entity.location()).setEntity(null);
		if(entity instanceof XHero)
			entitiesH.remove(entity);
		else if(entity instanceof XEnemy)
			entitiesE.remove(entity);
		else
			entities3.remove(entity);
	}

	public void moveEntity(XEntity entity, Tile newLocation)
	{
		XArrow arrow = XArrow.factory(entity.location(), newLocation, y1, false, entity.getImage());
		addArrow(arrow);
		entity.setReplacementArrow(arrow);
		advTile(entity.location()).setEntity(null);
		entity.setLocation(newLocation);
		advTile(newLocation).setEntity(entity);
	}

	public void swapEntities(XEntity entity1, XEntity entity2)
	{
		Tile location1 = entity1.location();
		Tile location2 = entity2.location();
		XArrow arrow1 = XArrow.factory(location1, location2, y1, false, entity1.getImage());
		addArrow(arrow1);
		entity1.setReplacementArrow(arrow1);
		XArrow arrow2 = XArrow.factory(location2, location1, y1, false, entity2.getImage());
		addArrow(arrow2);
		entity2.setReplacementArrow(arrow2);
		advTile(location1).setEntity(entity2);
		advTile(location2).setEntity(entity1);
		entity1.setLocation(location2);
		entity2.setLocation(location1);
	}

	public ArrayList<XHero> getEntitiesH()
	{
		return entitiesH;
	}

	public ArrayList<XEnemy> getEntitiesE()
	{
		return entitiesE;
	}

	public ArrayList<XEntity> getEntities3()
	{
		return entities3;
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
			for(XEntity entity : entitiesH)
			{
				a3 = entity.save(a3.startObject(), itemLoader, y1).end();
				xheroSave = entity.save3(xheroSave.startObject(), itemLoader).end();
			}
			for(XEntity entity : entitiesE)
			{
				a3 = entity.save(a3.startObject(), itemLoader, y1).end();
			}
			for(XEntity entity : entities3)
			{
				a3 = entity.save(a3.startObject(), itemLoader, y1).end();
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