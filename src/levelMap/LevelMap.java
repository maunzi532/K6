package levelMap;

import arrow.*;
import entity.*;
import geom.f1.*;
import java.util.*;

public class LevelMap
{
	private static final int TIME_PER_DISTANCE = 20;

	public final TileType y1;
	private final HashMap<Tile, AdvTile> advTiles;
	private Map<Tile, MarkType> marked;
	private final ArrayList<XArrow> arrows;

	public LevelMap(TileType y1)
	{
		this.y1 = y1;
		advTiles = new HashMap<>();
		marked = Map.of();
		arrows = new ArrayList<>();
	}

	public AdvTile advTile(Tile t1)
	{
		return advTiles.getOrDefault(t1, AdvTile.EMPTY);
	}

	public void productionPhase()
	{
		for(AdvTile advTile : advTiles.values())
		{
			if(advTile.getBuilding() != null)
				advTile.getBuilding().productionPhase(this);
		}
		for(AdvTile advTile : advTiles.values())
		{
			if(advTile.getBuilding() != null)
				advTile.getBuilding().afterProduction();
		}
	}

	public void transportPhase()
	{
		for(AdvTile advTile : advTiles.values())
		{
			if(advTile.getBuilding() != null)
				advTile.getBuilding().transportPhase(this);
		}
		for(AdvTile advTile : advTiles.values())
		{
			if(advTile.getBuilding() != null)
				advTile.getBuilding().afterTransport();
		}
	}

	public FloorTile getFloor(Tile t1)
	{
		return advTile(t1).getFloorTile();
	}

	public void addFloor(Tile t1, FloorTile floorTile)
	{
		advTiles.put(t1, new AdvTile(floorTile));
	}

	public MBuilding getBuilding(Tile t1)
	{
		return advTile(t1).getBuilding();
	}

	public void addBuilding(MBuilding building)
	{
		advTile(building.location()).setBuilding(building);
	}

	public MBuilding getOwner(Tile t1)
	{
		return advTile(t1).getOwned();
	}

	public void addOwner(Tile t1, MBuilding building)
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
	}

	public void removeEntity(XEntity entity)
	{
		advTile(entity.location()).setEntity(null);
	}

	public void moveEntity(XEntity entity, Tile newLocation)
	{
		removeEntity(entity);
		XArrow arrow = XArrow.factory(entity.location(), newLocation,
				y1.distance(newLocation, entity.location()) * TIME_PER_DISTANCE, false, entity.getImage(), false);
		addArrow(arrow);
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
}