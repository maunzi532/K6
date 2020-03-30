package geom.advtile;

import geom.tile.*;

public final class QuadLayout implements TileLayout
{
	public static final double Q2 = Math.sqrt(2);
	public static final double DQ2 = Math.sqrt(2) / 2;

	public final PointD size;
	public final PointD origin;

	public QuadLayout(PointD size, PointD origin)
	{
		this.size = size;
		this.origin = origin;
	}

	@Override
	public PointD size()
	{
		return size;
	}

	@Override
	public PointD tileToPixel(Tile t1)
	{
		double x0 = t1.v()[0] * Q2;
		double y0 = t1.v()[1] * Q2;
		return new PointD(x0 * size.v()[0] + origin.v()[0], y0 * size.v()[1] + origin.v()[1]);
	}

	@Override
	public PointD tileToPixel(DoubleTile t1)
	{
		double x0 = t1.v()[0] * Q2;
		double y0 = t1.v()[1] * Q2;
		return new PointD(x0 * size.v()[0] + origin.v()[0], y0 * size.v()[1] + origin.v()[1]);
	}

	@Override
	public DoubleTile pixelToTile(PointD p1, DoubleType y2)
	{
		PointD p2 = new PointD((p1.v()[0] - origin.v()[0]) / size.v()[0], (p1.v()[1] - origin.v()[1]) / size.v()[1]);
		double x = p2.v()[0] * DQ2;
		double y = p2.v()[1] * DQ2;
		return y2.createD(x, y);
	}

	@Override
	public PointD cornerOffset(int corner)
	{
		double angle = Math.PI * (0.5 + corner) / 2f;
		return new PointD(size.v()[0] * Math.cos(angle), size.v()[1] * Math.sin(angle));
	}

	@Override
	public int directionCount()
	{
		return 4;
	}

	@Override
	public PointD imageOffset()
	{
		return size;
	}
}