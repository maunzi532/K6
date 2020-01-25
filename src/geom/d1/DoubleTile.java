package geom.d1;

import java.util.*;

public record DoubleTile(double[] v)
{
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof DoubleTile that)) return false;
		return Arrays.equals(v, that.v);
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