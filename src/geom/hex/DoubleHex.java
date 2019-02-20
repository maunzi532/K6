package geom.hex;

public class DoubleHex
{
	public final double[] v;

	public DoubleHex(double x, double y, double z)
	{
		v = new double[]{x, y, z};
	}

	public DoubleHex(double[] v)
	{
		this.v = v;
	}

	public DoubleHex(Hex copy)
	{
		v = new double[]{copy.v[0], copy.v[1], copy.v[2]};
	}

	public Hex cast()
	{
		int x = (int) Math.round(v[0]);
		int y = (int) Math.round(v[1]);
		int z = (int) Math.round(v[2]);
		double xd = Math.abs(x - v[0]);
		double yd = Math.abs(y - v[1]);
		double zd = Math.abs(z - v[2]);
		if(xd > yd && zd <= xd)
		{
			x = -y - z;
		}
		else if(yd > zd)
		{
			y = -z - x;
		}
		else
		{
			z = -x - y;
		}
		return new Hex(x, y, z);
	}

	public DoubleHex add(DoubleHex h2)
	{
		return new DoubleHex(v[0] + h2.v[0], v[1] + h2.v[1], v[2] + h2.v[2]);
	}

	public DoubleHex subtract(DoubleHex minus)
	{
		return new DoubleHex(v[0] - minus.v[0], v[1] - minus.v[1], v[2] - minus.v[2]);
	}

	public DoubleHex multiply(double scalar)
	{
		return new DoubleHex(v[0] * scalar, v[1] * scalar, v[2] * scalar);
	}

	public double length()
	{
		return (Math.abs(v[0]) + Math.abs(v[1]) + Math.abs(v[2])) / 2;
	}

	public DoubleHex normalize()
	{
		double length = length();
		return new DoubleHex(v[0] / length, v[1] / length, v[2] / length);
	}

	public DoubleHex rotate(boolean inverse)
	{
		if(inverse)
			return new DoubleHex(v[1], v[2], v[0]);
		else
			return new DoubleHex(v[2], v[0], v[1]);
	}

	public static double lerp(double a, double b, double t)
	{
		return a * (1 - t) + b * t;
	}

	public static DoubleHex hexLerp(DoubleHex h1, DoubleHex h2, double t)
	{
		return new DoubleHex(lerp(h1.v[0], h2.v[0], t), lerp(h1.v[1], h2.v[1], t), lerp(h1.v[2], h2.v[2], t));
	}

	public static DoubleHex normalizeHex(Hex h1)
	{
		double length = h1.length();
		return new DoubleHex(h1.v[0] / length, h1.v[1] / length, h1.v[2] / length);
	}

	/*public static DoubleHex[] hexLine(Hex h1, Hex h2)
	{
		int n = h1.distance(h2);
		DoubleHex[] line = new DoubleHex[n + 1];
		double step = 1d / Math.max(n, 1);
		for(int i = 0; i <= n; i++)
		{
			line[i] = hexLerp(h1, h2, step * i);
		}
		return line;
	}*/
}