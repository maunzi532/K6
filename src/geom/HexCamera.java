package geom;

import geom.advtile.*;

public final class HexCamera extends TileCamera
{
	private final HexMatrix matrix;

	public HexCamera(XDimension dimension, double xMid, double yMid, double xSize, double ySize,
			double xShift, double yShift, HexMatrix matrix)
	{
		super(new HexDoubleType(), dimension, xMid, yMid, xSize, ySize, xShift, yShift);
		this.matrix = matrix;
	}

	@Override
	public int getRange()
	{
		double yDistance = dimension.yHW() / (ySize * dimension.scaleHW()) / 3;
		double xDistance = dimension.xHW() / (xSize * dimension.scaleHW()) / HexMatrix.Q3;
		return (int)(yDistance + xDistance + 1);
	}

	@Override
	public TileLayout layout(int screenshake)
	{
		return new HexLayout(matrix, new PointD(xSize(), ySize()),
				new PointD(dimension.xHW() * xMid - xShift * xSize() + screenshake, dimension.yHW() * yMid - yShift * ySize()));
	}

	@Override
	public int startMultiplier()
	{
		return -1;
	}
}