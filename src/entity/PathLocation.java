package entity;

import geom.f1.*;
import java.util.*;

public class PathLocation
{
	public final Tile tile;
	public final int cost;
	public final boolean canEnd;
	public final PathLocation from;
	public final XEntity movingAlly;

	public PathLocation(Tile tile)
	{
		this.tile = tile;
		cost = 0;
		canEnd = true;
		from = null;
		movingAlly = null;
	}

	public PathLocation(Tile tile, int cost, boolean canEnd, PathLocation from, XEntity movingAlly)
	{
		this.tile = tile;
		this.cost = cost;
		this.canEnd = canEnd;
		this.from = from;
		this.movingAlly = movingAlly;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof PathLocation)) return false;
		PathLocation that = (PathLocation) o;
		return tile.equals(that.tile);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(tile);
	}
}