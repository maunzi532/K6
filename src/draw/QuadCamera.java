package draw;

import geom.PointD;
import geom.quad.*;

public class QuadCamera
{
	public double xHalfWidth, yHalfWidth;
	public double xSize, ySize;
	public double xShift, yShift;
	public int range;

	public QuadCamera(double xHalfWidth, double yHalfWidth, double xSize, double ySize, double xShift, double yShift)
	{
		this.xHalfWidth = xHalfWidth;
		this.yHalfWidth = yHalfWidth;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xShift = xShift;
		this.yShift = yShift;
		determineRange();
	}

	public void determineRange()
	{
		double yDistance = yHalfWidth / ySize / 2;
		double xDistance = xHalfWidth / xSize / 2;
		range = (int) Math.max(yDistance, xDistance) + 1;
	}

	public QuadLayout layout()
	{
		return new QuadLayout(new PointD(xSize, ySize), new PointD(xHalfWidth - xShift * xSize, yHalfWidth - yShift * ySize));
	}

	public DoubleQuad clickLocation(double x, double y)
	{
		return layout().pixelToQuad(new PointD(x, y));
	}

	public Quad mid(QuadLayout layout)
	{
		return layout.pixelToQuad(new PointD(xHalfWidth, yHalfWidth)).cast();
	}
}