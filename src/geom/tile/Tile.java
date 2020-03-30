package geom.tile;

import java.util.*;

public record Tile(int[] v)
{
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!(obj instanceof Tile tile)) return false;
		return Arrays.equals(v, tile.v);
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(v);
	}

	@Override
	public String toString()
	{
		return Arrays.toString(v);
	}
}