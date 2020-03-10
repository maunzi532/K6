package geom.d1;

public record PointD(double[] v)
{
	public PointD(double x, double y)
	{
		this(new double[]{x, y});
	}

	public PointD add(PointD p1)
	{
		return new PointD(v[0] + p1.v[0], v[1] + p1.v[1]);
	}
}