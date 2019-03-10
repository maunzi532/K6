package entity;

import geom.hex.*;
import java.util.*;
import levelMap.*;

public class Pathing
{
	private final XEntity entity;
	private final Hex startLocation;
	private final int maxMovementCost;
	private final LevelMap map;
	private final Map<Hex, MarkType> endpoints;

	public Pathing(XEntity entity, int maxMovementCost, LevelMap map)
	{
		this(entity, entity.location(), maxMovementCost, map);
	}

	public Pathing(XEntity entity, Hex startLocation, int maxMovementCost, LevelMap map)
	{
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
			for(Hex neighbor : first.hex.neighbors())
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

	public Map<Hex, MarkType> getEndpoints()
	{
		return endpoints;
	}

	private PathLocation pathLocation(Hex hex, FullTile tile, int currentCost, int maxCost, XEntity entity)
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
		return new PathLocation(hex, cost, canEnd);
	}
}