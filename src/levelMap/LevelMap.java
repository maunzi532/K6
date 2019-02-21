package levelMap;

import arrow.*;
import geom.hex.Hex;
import java.util.*;

public class LevelMap
{
	private final HashMap<Hex, FloorTile> floor;
	private final HashMap<Hex, MBuilding> buildings;
	private final HashMap<Hex, MEntity> entities;
	private Set<Hex> marked;
	private final ArrayList<MArrow> arrows;

	public LevelMap()
	{
		floor = new HashMap<>();
		buildings = new HashMap<>();
		entities = new HashMap<>();
		marked = Set.of();
		arrows = new ArrayList<>();
	}

	public void moveEntity(MEntity entity, Hex newLocation)
	{
		entities.remove(entity.location());
		MArrow arrow = new VisualArrow(entity.location(), newLocation, ArrowMode.TRANSPORT, newLocation.distance(entity.location()) * 20, entity.getImage());
		arrows.add(arrow);
		entity.setLocation(newLocation);
		entity.setReplacementArrow(arrow);
		addEntity(entity);
	}

	public FullTile tile(Hex h1)
	{
		FloorTile floorTile = floor.get(h1);
		if(floorTile == null)
			return new FullTile();
		else
			return new FullTile(floorTile, buildings.get(h1), entities.get(h1), marked.contains(h1));
	}

	public void tickArrows()
	{
		arrows.removeIf(MArrow::tick);
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

	public void addFloor(Hex hex, FloorTile floorTile)
	{
		floor.put(hex, floorTile);
	}

	public void addBuilding(MBuilding building)
	{
		buildings.put(building.location(), building);
	}

	public void removeBuilding(MBuilding building)
	{
		buildings.remove(building.location());
	}

	public void addEntity(MEntity entity)
	{
		entities.put(entity.location(), entity);
	}

	public void setMarked(Set<Hex> marked)
	{
		Objects.requireNonNull(marked);
		this.marked = marked;
	}

	public void addArrow(MArrow arrow)
	{
		arrows.add(arrow);
	}

	public FloorTile getFloor(Hex hex)
	{
		return floor.get(hex);
	}

	public MBuilding getBuilding(Hex hex)
	{
		return buildings.get(hex);
	}

	public MEntity getEntity(Hex hex)
	{
		return entities.get(hex);
	}

	public Set<Hex> getMarked()
	{
		return marked;
	}

	public List<MArrow> getArrows()
	{
		return arrows;
	}
}