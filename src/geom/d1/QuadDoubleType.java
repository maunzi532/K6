package geom.d1;

import geom.f1.*;

public class QuadDoubleType implements DoubleType
{
	private QuadTileType tileType = new QuadTileType();

	private DoubleTile create2(double n1, double n2)
	{
		return new DoubleTile(new double[]{n1, n2});
	}

	@Override
	public TileType y1()
	{
		return tileType;
	}

	@Override
	public DoubleTile create(double[] v)
	{
		return new DoubleTile(v);
	}

	@Override
	public DoubleTile fromTile(Tile t1)
	{
		return create2(t1.v[0], t1.v[1]);
	}

	@Override
	public Tile cast(DoubleTile t1)
	{
		return y1().create2((int) Math.round(t1.v[0]), (int) Math.round(t1.v[1]));
	}

	@Override
	public DoubleTile add(DoubleTile t1, DoubleTile t2)
	{
		return create2(t1.v[0] + t2.v[0], t1.v[1] + t2.v[1]);
	}

	@Override
	public DoubleTile subtract(DoubleTile t1, DoubleTile minus)
	{
		return create2(t1.v[0] - minus.v[0], t1.v[1] - minus.v[1]);
	}

	@Override
	public DoubleTile multiply(DoubleTile t1, double scalar)
	{
		return create2(t1.v[0] * scalar, t1.v[1] * scalar);
	}

	@Override
	public double length(DoubleTile t1)
	{
		return Math.abs(t1.v[0]) + Math.abs(t1.v[1]);
	}

	@Override
	public DoubleTile normalize(DoubleTile t1)
	{
		double length = length(t1);
		return create2(t1.v[0] / length, t1.v[1] / length);
	}

	@Override
	public DoubleTile rotate(DoubleTile t1, boolean inverse)
	{
		throw new RuntimeException();
	}

	@Override
	public DoubleTile tileLerp(DoubleTile t1, DoubleTile t2, double t)
	{
		return create2(DoubleType.lerp(t1.v[0], t2.v[0], t), DoubleType.lerp(t1.v[1], t2.v[1], t));
	}
}