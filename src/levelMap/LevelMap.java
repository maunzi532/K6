package levelMap;

import arrow.*;
import building.*;
import entity.*;
import geom.hex.*;
import java.util.*;

public class LevelMap
{
	private static final int TIME_PER_DISTANCE = 20;

	private final HashMap<Hex, FloorTile> floor;
	private final HashMap<Hex, MBuilding> buildings;
	private final HashMap<Hex, MBuilding> ownedFloor;
	private final HashMap<Hex, MEntity> entities;
	private Map<Hex, MarkType> marked;
	private final ArrayList<MArrow> arrows;

	public LevelMap()
	{
		floor = new HashMap<>();
		buildings = new HashMap<>();
		ownedFloor = new HashMap<>();
		entities = new HashMap<>();
		marked = Map.of();
		arrows = new ArrayList<>();
	}

	public FullTile tile(Hex h1)
	{
		FloorTile floorTile = floor.get(h1);
		if(floorTile == null)
			return new FullTile();
		else
			return new FullTile(floorTile, buildings.get(h1), entities.get(h1), marked.get(h1));
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

	public FloorTile getFloor(Hex hex)
	{
		return floor.get(hex);
	}

	public void addFloor(Hex hex, FloorTile floorTile)
	{
		floor.put(hex, floorTile);
	}

	public MBuilding getBuilding(Hex hex)
	{
		return buildings.get(hex);
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

	public MBuilding getOwner(Hex hex)
	{
		return ownedFloor.get(hex);
	}

	public void addOwner(Hex hex, MBuilding building)
	{
		ownedFloor.put(hex, building);
	}

	public void removeOwner(Hex hex)
	{
		ownedFloor.remove(hex);
	}

	public MEntity getEntity(Hex hex)
	{
		return entities.get(hex);
	}

	public void addEntity(MEntity entity)
	{
		entities.put(entity.location(), entity);
	}

	public void removeEntity(MEntity entity)
	{
		entities.remove(entity.location());
	}

	public void moveEntity(MEntity entity, Hex newLocation)
	{
		entities.remove(entity.location());
		MArrow arrow = new VisualArrow(entity.location(), newLocation, ArrowMode.TRANSPORT,
				newLocation.distance(entity.location()) * TIME_PER_DISTANCE, entity.getImage());
		arrows.add(arrow);
		entity.setLocation(newLocation);
		entity.setReplacementArrow(arrow);
		addEntity(entity);
	}

	public Map<Hex, MarkType> getMarked()
	{
		return marked;
	}

	public void setMarked(Map<Hex, MarkType> marked)
	{
		Objects.requireNonNull(marked);
		this.marked = marked;
	}

	public List<MArrow> getArrows()
	{
		return arrows;
	}

	public void addArrow(MArrow arrow)
	{
		arrows.add(arrow);
	}

	public void tickArrows()
	{
		arrows.removeIf(MArrow::tick);
	}
}