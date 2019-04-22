package geom;

import geom.d1.*;

public class QuadCamera extends TileCamera
{
	public QuadCamera(XDimension dimension, double xSize, double ySize,
			double xShift, double yShift)
	{
		super(new QuadDoubleType(), dimension, xSize, ySize, xShift, yShift);
	}

	@Override
	public int getRange()
	{
		double yDistance = dimension.yHW() / ySize / QuadLayout.Q2;
		double xDistance = dimension.xHW() / xSize / QuadLayout.Q2;
		return (int)(Math.max(yDistance, xDistance) + 1);
	}

	@Override
	public TileLayout layout()
	{
		return new QuadLayout(new PointD(xSize, ySize), new PointD(dimension.xHW() - xShift * xSize, dimension.yHW() - yShift * ySize));
	}
}