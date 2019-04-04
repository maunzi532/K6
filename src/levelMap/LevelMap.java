package levelMap;

import arrow.*;
import building.*;
import entity.*;
import geom.d1.*;
import geom.f1.*;
import java.util.*;

public class LevelMap
{
	private static final int TIME_PER_DISTANCE = 20;

	public final TileType y1;
	public final DoubleType y2;
	private final HashMap<Tile, FloorTile> floor;
	private final HashMap<Tile, MBuilding> buildings;
	private final HashMap<Tile, MBuilding> ownedFloor;
	private final HashMap<Tile, XEntity> entities;
	private Map<Tile, MarkType> marked;
	private final ArrayList<VisualArrow> arrows;

	public LevelMap(TileType y1, DoubleType y2)
	{
		this.y1 = y1;
		this.y2 = y2;
		floor = new HashMap<>();
		buildings = new HashMap<>();
		ownedFloor = new HashMap<>();
		entities = new HashMap<>();
		marked = Map.of();
		arrows = new ArrayList<>();
	}

	public FullTile tile(Tile t1)
	{
		FloorTile floorTile = floor.get(t1);
		if(floorTile == null)
			return new FullTile();
		else
			return new FullTile(floorTile, buildings.get(t1), entities.get(t1), marked.get(t1));
	}

	public void productionPhase()
	{
		for(MBuilding building : buildings.values())
		{
			building.productionPhase(this);
		}
		for(MBuilding building : buildings.values())
		{
			building.afterProduction();
		}
	}

	public void transportPhase()
	{
		for(MBuilding building : buildings.values())
		{
			building.transportPhase(this);
		}
		for(MBuilding building : buildings.values())
		{
			building.afterTransport();
		}
	}

	public FloorTile getFloor(Tile t1)
	{
		return floor.get(t1);
	}

	public void addFloor(Tile t1, FloorTile floorTile)
	{
		floor.put(t1, floorTile);
	}

	public MBuilding getBuilding(Tile t1)
	{
		return buildings.get(t1);
	}

	public void addBuilding(MBuilding building)
	{
		buildings.put(building.location(), building);
	}

	public void removeBuilding(MBuilding building)
	{
		if(building instanceof ProductionBuilding)
		{
			ownedFloor.keySet().removeAll(((ProductionBuilding) building).getClaimed());
		}
		buildings.remove(building.location());
	}

	public MBuilding getOwner(Tile t1)
	{
		return ownedFloor.get(t1);
	}

	public void addOwner(Tile t1, MBuilding building)
	{
		ownedFloor.put(t1, building);
	}

	public void removeOwner(Tile t1)
	{
		ownedFloor.remove(t1);
	}

	public XEntity getEntity(Tile t1)
	{
		return entities.get(t1);
	}

	public void addEntity(XEntity entity)
	{
		entities.put(entity.location(), entity);
	}

	public void removeEntity(XEntity entity)
	{
		entities.remove(entity.location());
	}

	public void moveEntity(XEntity entity, Tile newLocation)
	{
		entities.remove(entity.location());
		VisualArrow arrow = new VisualArrow(y2, entity.location(), newLocation, ArrowMode.TRANSPORT,
				y1.distance(newLocation, entity.location()) * TIME_PER_DISTANCE, entity.getImage());
		arrows.add(arrow);
		entity.setLocation(newLocation);
		entity.setReplacementArrow(arrow);
		addEntity(entity);
	}

	public Map<Tile, MarkType> getMarked()
	{
		return marked;
	}

	public void setMarked(Map<Tile, MarkType> marked)
	{
		Objects.requireNonNull(marked);
		this.marked = marked;
	}

	public List<VisualArrow> getArrows()
	{
		return arrows;
	}

	public void addArrow(VisualArrow arrow)
	{
		arrows.add(arrow);
	}

	public void tickArrows()
	{
		arrows.removeIf(VisualArrow::tick);
	}
}