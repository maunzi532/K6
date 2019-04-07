package geom;

import geom.d1.*;

public class HexCamera extends TileCamera
{
	private HexMatrix matrix;

	public HexCamera(double xHalfWidth, double yHalfWidth, double xSize, double ySize,
			double xShift, double yShift, HexMatrix matrix)
	{
		super(new HexDoubleType(), xHalfWidth, yHalfWidth, xSize, ySize, xShift, yShift);
		this.matrix = matrix;
	}

	@Override
	public int getRange()
	{
		double yDistance = yHalfWidth / ySize / 3;
		double xDistance = xHalfWidth / xSize / HexMatrix.Q3;
		return (int)(yDistance + xDistance + 1);
	}

	@Override
	public TileLayout layout()
	{
		return new HexLayout(matrix, new PointD(xSize, ySize),
				new PointD(xHalfWidth - xShift * xSize, yHalfWidth - yShift * ySize));
	}
}