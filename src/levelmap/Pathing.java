package levelmap;

import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;

public final class Pathing
{
	private final XCharacter entity;
	private final Tile startLocation;
	private final int maxMovementCost;
	private final LevelMap map;
	private final boolean allyEndpoints;
	private List<Tile> endpoints;
	private List<PathLocation> endpaths;

	public Pathing(XCharacter entity, int maxMovementCost, LevelMap map, boolean allyEndpoints)
	{
		this.entity = entity;
		startLocation = entity.location();
		this.maxMovementCost = maxMovementCost;
		this.map = map;
		this.allyEndpoints = allyEndpoints;
	}

	public List<Tile> getEndpoints()
	{
		start();
		return endpoints;
	}

	public List<PathLocation> getEndpaths()
	{
		start();
		return endpaths;
	}

	private void start()
	{
		List<PathLocation> lA = new ArrayList<>();
		endpoints = new ArrayList<>();
		lA.add(new PathLocation(startLocation));
		endpoints.add(startLocation);
		for(int counter = 0; counter < lA.size(); counter++)
		{
			PathLocation first = lA.get(counter);
			if(first != null)
			{
				for(Tile neighbor : map.y1().neighbors(first.tile()))
				{
					PathLocation pl = pathLocation(neighbor, map.advTile(neighbor),
							first.cost(), maxMovementCost, entity, first, allyEndpoints);
					if(pl != null)
					{
						int prevIndex = lA.indexOf(pl);
						if(prevIndex >= 0)
						{
							if(pl.cost() < lA.get(prevIndex).cost())
							{
								lA.set(prevIndex, null);
								lA.add(pl);
							}
						}
						else
						{
							lA.add(pl);
							if(pl.canEnd())
								endpoints.add(neighbor);
						}
					}
				}
			}
		}
		endpaths = lA.stream().filter(e -> e != null && endpoints.contains(e.tile())).collect(Collectors.toList());
	}

	private static PathLocation pathLocation(Tile t1, AdvTile advTile, int currentCost, int maxCost,
			XCharacter entity, PathLocation from, boolean allyEndpoints)
	{
		if(advTile.floorTile() == null || advTile.floorTile().blocked())
			return null;
		boolean canEnd;
		if(advTile.entity() != null && advTile.entity() != entity)
		{
			if(entity.isEnemy(advTile.entity()))
				return null;
			else
				canEnd = allyEndpoints && advTile.floorTile().canMovementEnd();
		}
		else
		{
			canEnd = advTile.floorTile().canMovementEnd();
		}
		int cost = currentCost + advTile.floorTile().moveCost();
		if(cost > maxCost)
			return null;
		return new PathLocation(t1, cost, canEnd, from);
	}
}