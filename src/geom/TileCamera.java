package geom;

import geom.d1.*;
import geom.f1.*;

public abstract class TileCamera
{
	protected DoubleType y2;
	protected XDimension dimension;
	protected double xMid, yMid;
	protected double xSize, ySize;
	protected double xShift, yShift;
	private double zoom;

	public TileCamera(DoubleType y2, XDimension dimension, double xMid, double yMid, double xSize, double ySize,
			double xShift, double yShift)
	{
		this.y2 = y2;
		this.dimension = dimension;
		this.xMid = xMid;
		this.yMid = yMid;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xShift = xShift;
		this.yShift = yShift;
		zoom = 1;
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

	public double getXShift()
	{
		return xShift;
	}

	public double getYShift()
	{
		return yShift;
	}

	public double xSize()
	{
		return xSize * zoom;
	}

	public double ySize()
	{
		return ySize * zoom;
	}

	public abstract int getRange();

	public abstract TileLayout layout(int screenshake);

	public DoubleTile clickLocation(double x, double y)
	{
		return layout(0).pixelToTile(new PointD(x, y), y2);
	}

	public DoubleTile clickLocation(double x, double y, int screenshake)
	{
		return layout(screenshake).pixelToTile(new PointD(x, y), y2);
	}

	public Tile mid(TileLayout layout)
	{
		return y2.cast(layout.pixelToTile(new PointD(dimension.xHW() * xMid, dimension.yHW() * yMid), y2));
	}

	public void setZoom(double zoom)
	{
		this.zoom = zoom;
	}
}