package geom;

import geom.d1.*;
import geom.f1.*;

public abstract class TileCamera
{
	protected DoubleType y2;
	protected XDimension dimension;
	protected double xSize, ySize;
	protected double xShift, yShift;

	public TileCamera(DoubleType y2, XDimension dimension, double xSize, double ySize,
			double xShift, double yShift)
	{
		this.y2 = y2;
		this.dimension = dimension;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xShift = xShift;
		this.yShift = yShift;
	}

	public DoubleType getDoubleType()
	{
		return y2;
	}

	public void setXShift(double xShift)
	{
		this.xShift = xShift;
	}

	public void setYShift(double yShift)
	{
		this.yShift = yShift;
	}

	public abstract int getRange();

	public abstract TileLayout layout();

	public DoubleTile clickLocation(double x, double y)
	{
		return layout().pixelToTile(new PointD(x, y), y2);
	}

	public Tile mid(TileLayout layout)
	{
		return y2.cast(layout.pixelToTile(new PointD(dimension.xHW(), dimension.yHW()), y2));
	}
}