package draw;

import geom.PointD;
import geom.hex.*;

public class HexCamera
{
	private double xHalfWidth, yHalfWidth;
	private double xSize, ySize;
	public double xShift, yShift;
	private HexMatrix matrix;

	public HexCamera(double xHalfWidth, double yHalfWidth, double xSize, double ySize, double xShift, double yShift, HexMatrix matrix)
	{
		this.xHalfWidth = xHalfWidth;
		this.yHalfWidth = yHalfWidth;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xShift = xShift;
		this.yShift = yShift;
		this.matrix = matrix;
	}

	public int getRange()
	{
		double yDistance = yHalfWidth / ySize / 3;
		double xDistance = xHalfWidth / xSize / HexMatrix.Q3;
		return (int)(yDistance + xDistance + 1);
	}

	public HexLayout layout()
	{
		return new HexLayout(matrix, new PointD(xSize, ySize),
				new PointD(xHalfWidth - xShift * xSize, yHalfWidth - yShift * ySize));
	}

	public DoubleHex clickLocation(double x, double y)
	{
		return layout().pixelToHex(new PointD(x, y));
	}

	public Hex mid(HexLayout layout)
	{
		return layout.pixelToHex(new PointD(xHalfWidth, yHalfWidth)).cast();
	}
}