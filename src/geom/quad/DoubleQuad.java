package geom.quad;

public class DoubleQuad
{
	public final double[] v;

	public DoubleQuad(double x, double y)
	{
		v = new double[]{x, y};
	}

	public DoubleQuad(double[] v)
	{
		this.v = v;
	}

	public DoubleQuad(Quad copy)
	{
		v = new double[]{copy.v[0], copy.v[1]};
	}

	public Quad cast()
	{
		return new Quad((int) Math.round(v[0]), (int) Math.round(v[1]));
	}

	public DoubleQuad add(DoubleQuad h2)
	{
		return new DoubleQuad(v[0] + h2.v[0], v[1] + h2.v[1]);
	}

	public DoubleQuad subtract(DoubleQuad minus)
	{
		return new DoubleQuad(v[0] - minus.v[0], v[1] - minus.v[1]);
	}

	public DoubleQuad multiply(double scalar)
	{
		return new DoubleQuad(v[0] * scalar, v[1] * scalar);
	}

	public double length()
	{
		return Math.abs(v[0]) + Math.abs(v[1]);
	}

	public DoubleQuad normalize()
	{
		double length = length();
		return new DoubleQuad(v[0] / length, v[1] / length);
	}

	public static double lerp(double a, double b, double t)
	{
		return a * (1 - t) + b * t;
	}

	public static DoubleQuad quadLerp(Quad h1, Quad h2, double t)
	{
		return new DoubleQuad(lerp(h1.v[0], h2.v[0], t), lerp(h1.v[1], h2.v[1], t));
	}

	public static DoubleQuad[] quadLine(Quad h1, Quad h2)
	{
		int n = h1.distance(h2);
		DoubleQuad[] line = new DoubleQuad[n + 1];
		double step = 1d / Math.max(n, 1);
		for(int i = 0; i <= n; i++)
		{
			line[i] = quadLerp(h1, h2, step * i);
		}
		return line;
	}
}