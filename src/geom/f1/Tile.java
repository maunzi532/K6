package geom.f1;

import java.util.*;

public record Tile(int[] v)
{
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Tile tile)) return false;
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