package levelMap;

import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;

public class Pathing
{
	public final TileType y1;
	private final XEntity entity;
	private final Tile startLocation;
	private final int maxMovementCost;
	private final LevelMap map;
	private final List<XEntity> movingAllies;
	private List<Tile> endpoints;
	private List<PathLocation> endpaths;

	public Pathing(TileType y1, XEntity entity, int maxMovementCost, LevelMap map, List<XEntity> movingAllies)
	{
		this(y1, entity, entity.location(), maxMovementCost, map, movingAllies);
	}

	public Pathing(TileType y1, XEntity entity, Tile startLocation,
			int maxMovementCost, LevelMap map, List<XEntity> movingAllies)
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
			if(first == null)
				continue;
			for(Tile neighbor : y1.neighbors(first.tile()))
			{
				PathLocation pl = pathLocation(neighbor, map.advTile(neighbor), first.cost(), maxMovementCost, entity, first, movingAllies);
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
			XEntity entity, PathLocation from, List<XEntity> movingAllies)
	{
		if(advTile.getFloorTile() == null || advTile.getFloorTile().blocked())
			return null;
		boolean canEnd = advTile.getFloorTile().canMovementEnd();
		XEntity movingAlly = null;
		if(advTile.getEntity() != null && advTile.getEntity() != entity)
		{
			if(entity.isEnemy(advTile.getEntity()))
				return null;
			else if(movingAllies.contains(advTile.getEntity()))
				movingAlly = advTile.getEntity();
			else
				canEnd = false;
		}
		int cost = currentCost + advTile.getFloorTile().moveCost();
		if(cost > maxCost)
			return null;
		return new PathLocation(t1, cost, canEnd, from, movingAlly);
	}
}