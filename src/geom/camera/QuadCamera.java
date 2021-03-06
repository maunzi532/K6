package geom.camera;

import geom.layout.*;
import geom.dtile.*;

public final class QuadCamera extends TileCamera
{
	public QuadCamera(XDimension dimension, double xMid, double yMid, double xSize, double ySize,
			double xShift, double yShift)
	{
		super(new QuadDoubleType(), dimension, xMid, yMid, xSize, ySize, xShift, yShift);
	}

	@Override
	public int getRange()
	{
		double yDistance = dimension.yHW() / (ySize * dimension.scaleHW()) / QuadLayout.Q2;
		double xDistance = dimension.xHW() / (xSize * dimension.scaleHW()) / QuadLayout.Q2;
		return (int)(Math.max(yDistance, xDistance) + 1.0);
	}

	@Override
	public TileLayout layout(int screenshake)
	{
		return new QuadLayout(new PointD(xSize(), ySize()), new PointD(dimension.xHW() * xMid - xShift * xSize() + screenshake,
				dimension.yHW() * yMid - yShift * ySize()));
	}

	@Override
	public int startMultiplier()
	{
		return 0;
	}
}