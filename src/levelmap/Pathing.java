package levelmap;

import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;

public final class Pathing
{
	private final TileType y1;
	private final XCharacter entity;
	private final Tile startLocation;
	private final int maxMovementCost;
	private final LevelMap map;
	private final List<XCharacter> movingAllies;
	private List<Tile> endpoints;
	private List<PathLocation> endpaths;

	public Pathing(TileType y1, XCharacter entity, int maxMovementCost, LevelMap map, List<XCharacter> movingAllies)
	{
		this(y1, entity, entity.location(), maxMovementCost, map, movingAllies);
	}

	public Pathing(TileType y1, XCharacter entity, Tile startLocation,
			int maxMovementCost, LevelMap map, List<XCharacter> movingAllies)
	{
		this.y1 = y1;
		this.entity = entity;
		this.startLocation = startLocation;
		this.maxMovementCost = maxMovementCost;
		this.map = map;
		this.movingAllies = movingAllies != null ? movingAllies : List.of();
	}

	public Pathing start()
	{
		List<PathLocation> lA = new ArrayList<>();
		endpoints = new ArrayList<>();
		lA.add(new PathLocation(startLocation, 0, true, null, null));
		endpoints.add(startLocation);
		for(int counter = 0; counter < lA.size(); counter++)
		{
			PathLocation first = lA.get(counter);
			if(first != null)
			{
				for(Tile neighbor : y1.neighbors(first.tile()))
				{
					PathLocation pl = pathLocation(neighbor, map.advTile(neighbor),
							first.cost(), maxMovementCost, entity, first, movingAllies);
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
		return this;
	}

	public List<Tile> getEndpoints()
	{
		return endpoints;
	}

	public List<PathLocation> getEndpaths()
	{
		return endpaths;
	}

	private static PathLocation pathLocation(Tile t1, AdvTile advTile, int currentCost, int maxCost,
			XCharacter entity, PathLocation from, List<XCharacter> movingAllies)
	{
		if(advTile.floorTile() == null || advTile.floorTile().blocked())
			return null;
		boolean canEnd = advTile.floorTile().canMovementEnd();
		XCharacter movingAlly = null;
		if(advTile.entity() != null && advTile.entity() != entity)
		{
			if(entity.isEnemy(advTile.entity()))
				return null;
			else if(movingAllies.contains(advTile.entity()))
				movingAlly = advTile.entity();
			else
				canEnd = false;
		}
		int cost = currentCost + advTile.floorTile().moveCost();
		if(cost > maxCost)
			return null;
		return new PathLocation(t1, cost, canEnd, from, movingAlly);
	}
}