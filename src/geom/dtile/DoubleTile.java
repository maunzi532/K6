package geom.dtile;

import java.util.*;

public record DoubleTile(double[] v)
{
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!(obj instanceof DoubleTile other)) return false;
		return Arrays.equals(v, other.v);
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