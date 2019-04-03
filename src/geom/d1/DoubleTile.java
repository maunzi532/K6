package geom.d1;

import java.util.*;

public class DoubleTile
{
	public final double[] v;

	public DoubleTile(double[] v)
	{
		this.v = v;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof DoubleTile)) return false;
		DoubleTile that = (DoubleTile) o;
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