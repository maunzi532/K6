package geom;

import geom.d1.*;

public class QuadCamera extends TileCamera
{
	public QuadCamera(double xHalfWidth, double yHalfWidth, double xSize, double ySize,
			double xShift, double yShift)
	{
		super(new QuadDoubleType(), xHalfWidth, yHalfWidth, xSize, ySize, xShift, yShift);
	}

	@Override
	public int getRange()
	{
		double yDistance = yHalfWidth / ySize / QuadLayout.Q2;
		double xDistance = xHalfWidth / xSize / QuadLayout.Q2;
		return (int)(Math.max(yDistance, xDistance) + 1);
	}

	@Override
	public TileLayout layout()
	{
		return new QuadLayout(new PointD(xSize, ySize), new PointD(xHalfWidth - xShift * xSize, yHalfWidth - yShift * ySize));
	}
}