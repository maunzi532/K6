package draw;

import geom.PointD;
import geom.quad.*;

public class QuadCamera
{
	private double xHalfWidth, yHalfWidth;
	private double xSize, ySize;
	public double xShift, yShift;

	public QuadCamera(double xHalfWidth, double yHalfWidth, double xSize, double ySize, double xShift, double yShift)
	{
		this.xHalfWidth = xHalfWidth;
		this.yHalfWidth = yHalfWidth;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xShift = xShift;
		this.yShift = yShift;
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