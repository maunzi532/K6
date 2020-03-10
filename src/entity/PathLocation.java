package entity;

import geom.f1.*;
import java.util.*;

public record PathLocation(Tile tile, int cost, boolean canEnd, PathLocation from, XEntity movingAlly)
{
	public PathLocation(Tile tile)
	{
		this(tile, 0, true, null, null);
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof PathLocation that)) return false;
		return tile.equals(that.tile);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(tile);
	}
}