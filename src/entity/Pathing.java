package entity;

import geom.f1.*;
import java.util.*;
import levelMap.*;

public class Pathing
{
	public final TileType y1;
	private final XEntity entity;
	private final Tile startLocation;
	private final int maxMovementCost;
	private final LevelMap map;
	private final Map<Tile, MarkType> endpoints;

	public Pathing(TileType y1, XEntity entity, int maxMovementCost, LevelMap map)
	{
		this(y1, entity, entity.location(), maxMovementCost, map);
	}

	public Pathing(TileType y1, XEntity entity, Tile startLocation, int maxMovementCost, LevelMap map)
	{
		this.y1 = y1;
		this.entity = entity;
		this.startLocation = startLocation;
		this.maxMovementCost = maxMovementCost;
		this.map = map;
		endpoints = new HashMap<>();
	}

	public Pathing start()
	{
		List<PathLocation> lA = new ArrayList<>();
		lA.add(new PathLocation(startLocation, 0, true));
		endpoints.put(startLocation, MarkType.TARGET);
		for(int counter = 0; counter < lA.size(); counter++)
		{
			PathLocation first = lA.get(counter);
			if(first == null)
				continue;
			for(Tile neighbor : y1.neighbors(first.t1))
			{
				PathLocation pl = pathLocation(neighbor, map.tile(neighbor), first.cost, maxMovementCost, entity);
				if(pl != null)
				{
					int prevIndex = lA.indexOf(pl);
					if(prevIndex >= 0)
					{
						if(pl.cost < lA.get(prevIndex).cost)
						{
							lA.set(prevIndex, null);
							lA.add(pl);
						}
					}
					else
					{
						lA.add(pl);
						if(pl.canEnd)
							endpoints.put(neighbor, MarkType.TARGET);
					}
				}
			}
		}
		return this;
	}

	public void copyIntoMap()
	{
		map.setMarked(endpoints);
	}

	public Map<Tile, MarkType> getEndpoints()
	{
		return endpoints;
	}

	private PathLocation pathLocation(Tile t1, FullTile tile, int currentCost, int maxCost, XEntity entity)
	{
		if(!tile.exists())
			return null;
		int cost = currentCost + tile.floorTile.moveCost();
		if(cost > maxCost)
			return null;
		boolean canEnd = tile.floorTile.canMovementEnd();
		if(tile.entity != null && tile.entity != entity)
		{
			if(entity.isEnemy(tile.entity))
				return null;
			else
				canEnd = false;
		}
		return new PathLocation(t1, cost, canEnd);
	}
}