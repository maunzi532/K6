package geom.dtile;

import geom.tile.*;

public final class HexDoubleType extends HexTileType implements DoubleType
{
	private DoubleTile create3d(double n1, double n2, double n3)
	{
		return new DoubleTile(new double[]{n1, n2, n3});
	}

	@Override
	public DoubleTile createD(double... v)
	{
		return new DoubleTile(v);
	}

	@Override
	public DoubleTile fromTile(Tile t1)
	{
		return create3d(t1.v()[0], t1.v()[1], t1.v()[2]);
	}

	@Override
	public Tile cast(DoubleTile t1)
	{
		int x = (int) Math.round(t1.v()[0]);
		int y = (int) Math.round(t1.v()[1]);
		int z = (int) Math.round(t1.v()[2]);
		double xd = Math.abs(x - t1.v()[0]);
		double yd = Math.abs(y - t1.v()[1]);
		double zd = Math.abs(z - t1.v()[2]);
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
		return create3(x, y, z);
	}

	@Override
	public DoubleTile add(DoubleTile t1, DoubleTile t2)
	{
		return create3d(t1.v()[0] + t2.v()[0], t1.v()[1] + t2.v()[1], t1.v()[2] + t2.v()[2]);
	}

	@Override
	public DoubleTile subtract(DoubleTile t1, DoubleTile minus)
	{
		return create3d(t1.v()[0] - minus.v()[0], t1.v()[1] - minus.v()[1], t1.v()[2] - minus.v()[2]);
	}

	@Override
	public DoubleTile multiply(DoubleTile t1, double scalar)
	{
		return create3d(t1.v()[0] * scalar, t1.v()[1] * scalar, t1.v()[2] * scalar);
	}

	private double length(DoubleTile t1)
	{
		return (Math.abs(t1.v()[0]) + Math.abs(t1.v()[1]) + Math.abs(t1.v()[2])) / 2.0;
	}

	@Override
	public DoubleTile normalize(DoubleTile t1)
	{
		double length = length(t1);
		return create3d(t1.v()[0] / length, t1.v()[1] / length, t1.v()[2] / length);
	}

	private DoubleTile rotate(DoubleTile t1, boolean inverse)
	{
		if(inverse)
			return create3d(t1.v()[1], t1.v()[2], t1.v()[0]);
		else
			return create3d(t1.v()[2], t1.v()[0], t1.v()[1]);
	}

	@Override
	public DoubleTile rotateR2(DoubleTile t1, boolean inverse)
	{
		return subtract(rotate(t1, false), rotate(t1, true));
	}

	@Override
	public DoubleTile upwards()
	{
		return createD(1.0, 1.0, -2.0);
	}

	@Override
	public DoubleTile tileLerp(DoubleTile t1, DoubleTile t2, double t)
	{
		return create3d(DoubleType.lerp(t1.v()[0], t2.v()[0], t),
				DoubleType.lerp(t1.v()[1], t2.v()[1], t),
				DoubleType.lerp(t1.v()[2], t2.v()[2], t));
	}

	@Override
	public DoubleTile fromOffsetD(double x, double y)
	{
		return create3d(x - y / 2.0, -x - x / 2.0, y);
	}
}