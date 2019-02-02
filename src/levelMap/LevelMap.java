package levelMap;

import arrow.*;
import building.*;
import hex.*;
import java.util.*;
import entity.*;

public class LevelMap implements CanAddArrows
{
	private final HashMap<Hex, FloorTile> floor;
	private final HashMap<Hex, Building> buildings;
	private final HashMap<Hex, XEntity> entities;
	private Set<Hex> marked;
	private final ArrayList<VisualArrow> arrows;

	public LevelMap()
	{
		floor = new HashMap<>();
		buildings = new HashMap<>();
		entities = new HashMap<>();
		marked = Set.of();
		arrows = new ArrayList<>();
	}

	public void moveEntity(XEntity entity, Hex newLocation)
	{
		entities.remove(entity.location);
		VisualArrow arrow = new VisualArrow(entity.location, newLocation, ArrowMode.TRANSPORT, newLocation.distance(entity.location) * 20, XEntity.IMAGE);
		arrows.add(arrow);
		entity.location = newLocation;
		entity.replace = arrow;
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
		arrows.removeIf(VisualArrow::tick);
	}

	public void buildingPhase()
	{
		for(Building building : buildings.values())
		{
			building.productionPhase(this);
		}
		for(Building building : buildings.values())
		{
			building.afterProduction();
		}
	}

	public void transportPhase()
	{
		for(Building building : buildings.values())
		{
			building.transportPhase(this);
		}
		for(Building building : buildings.values())
		{
			building.afterTransport();
		}
	}

	public void addFloor(Hex hex, FloorTile floorTile)
	{
		floor.put(hex, floorTile);
	}

	public void addBuilding(Hex hex, Building building)
	{
		buildings.put(hex, building);
	}

	public void addEntity(XEntity entity)
	{
		entities.put(entity.location, entity);
	}

	public void setMarked(Set<Hex> marked)
	{
		Objects.requireNonNull(marked);
		this.marked = marked;
	}

	@Override
	public void addArrow(VisualArrow arrow)
	{
		arrows.add(arrow);
	}

	public Set<Hex> getMarked()
	{
		return marked;
	}

	public List<VisualArrow> getArrows()
	{
		return arrows;
	}
}