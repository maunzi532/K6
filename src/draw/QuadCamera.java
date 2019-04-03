package draw;

import geom.*;
import geom.d1.*;
import geom.f1.*;

public class QuadCamera implements TileCamera
{
	private DoubleType y2;
	private double xHalfWidth, yHalfWidth;
	private double xSize, ySize;
	public double xShift, yShift;

	public QuadCamera(DoubleType y2, double xHalfWidth, double yHalfWidth, double xSize, double ySize,
			double xShift, double yShift)
	{
		this.y2 = y2;
		this.xHalfWidth = xHalfWidth;
		this.yHalfWidth = yHalfWidth;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xShift = xShift;
		this.yShift = yShift;
	}

	@Override
	public void setXShift(double xShift)
	{
		this.xShift = xShift;
	}

	@Override
	public void setYShift(double yShift)
	{
		this.yShift = yShift;
	}

	@Override
	public int getRange()
	{
		double yDistance = yHalfWidth / ySize / 2;
		double xDistance = xHalfWidth / xSize / 2; //TODO ???
		return (int)(yDistance + xDistance + 1);
	}

	@Override
	public TileLayout layout()
	{
		return new QuadLayout2(new PointD(xSize, ySize), new PointD(xHalfWidth - xShift * xSize, yHalfWidth - yShift * ySize));
	}

	@Override
	public DoubleTile clickLocation(double x, double y)
	{
		return layout().pixelToTile(new PointD(x, y), y2);
	}

	@Override
	public Tile mid(TileLayout layout)
	{
		return y2.cast(layout.pixelToTile(new PointD(xHalfWidth, yHalfWidth), y2));
	}
}