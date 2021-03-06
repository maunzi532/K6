package entity;

import geom.tile.*;

public record PathLocation(Tile tile, int cost, boolean canEnd, PathLocation from)
{
	public PathLocation(Tile tile)
	{
		this(tile, 0, true, null);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!(obj instanceof PathLocation other)) return false;
		return tile.equals(other.tile);
	}

	@Override
	public int hashCode()
	{
		return tile.hashCode();
	}
}