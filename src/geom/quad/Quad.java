package geom.quad;

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

	public Quad(Quad copy)
	{
		v = new int[2];
		System.arraycopy(copy.v, 0, v, 0, 2);
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
}