package geom;

public class PointD
{
	public final double[] v;

	public PointD(double x, double y)
	{
		v = new double[]{x, y};
	}

	public PointD add(PointD p1)
	{
		return new PointD(v[0] + p1.v[0], v[1] + p1.v[1]);
	}
}