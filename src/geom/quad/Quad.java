package geom.quad;

import java.util.*;

public class Quad
{
	public final int[] v;

	public Quad(int x, int y)
	{
		v = new int[]{x, y};
	}

	public Quad(int[] v)
	{
		assert v.length == 2;
		this.v = v;
	}

	public Quad add(Quad h2)
	{
		return new Quad(v[0] + h2.v[0], v[1] + h2.v[1]);
	}

	public Quad subtract(Quad minus)
	{
		return new Quad(v[0] - minus.v[0], v[1] - minus.v[1]);
	}

	public Quad multiply(int scalar)
	{
		return new Quad(v[0] * scalar, v[1] * scalar);
	}

	public int length()
	{
		return Math.abs(v[0]) + Math.abs(v[1]);
	}

	public int distance(Quad h2)
	{
		return subtract(h2).length();
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Quad)) return false;
		Quad quad = (Quad) o;
		return Arrays.equals(v, quad.v);
	}

	@Override
	public int hashCode()
	{
		return (v[0] << 16) + v[1];
	}

	@Override
	public String toString()
	{
		return v[0] + ", " + v[1];
	}
}