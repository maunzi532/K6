package geom.f1;

import java.util.*;

public class Tile
{
	public final int[] v;

	public Tile(int[] v)
	{
		this.v = v;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Tile)) return false;
		Tile tile = (Tile) o;
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