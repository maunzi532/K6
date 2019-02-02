package draw;

import hex.*;

public class XCamera
{
	public double xHalfWidth, yHalfWidth;
	public double xSize, ySize;
	public double xShift, yShift;
	public MatrixH matrix;
	public int range;

	public XCamera(double xHalfWidth, double yHalfWidth, double xSize, double ySize, double xShift, double yShift, MatrixH matrix)
	{
		this.xHalfWidth = xHalfWidth;
		this.yHalfWidth = yHalfWidth;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xShift = xShift;
		this.yShift = yShift;
		this.matrix = matrix;
		determineRange();
	}

	public void determineRange()
	{
		double yDistance = yHalfWidth / ySize / 3;
		double xDistance = xHalfWidth / xSize / MatrixH.Q3;
		range = (int)(yDistance + xDistance + 1);
	}

	public LayoutH layout()
	{
		//MatrixH matrix = MatrixH.layoutLerp(MatrixH.layoutPointy(), MatrixH.layoutFlat(), Math.abs((timer % 100) - 50) / 50d);
		return new LayoutH(matrix, new PointD(xSize, ySize),
				new PointD(xHalfWidth - xShift * xSize, yHalfWidth - yShift * ySize));
	}

	public DoubleHex clickLocation(double x, double y)
	{
		return layout().pixelToHex(new PointD(x, y));
	}

	public Hex mid(LayoutH layout)
	{
		return layout.pixelToHex(new PointD(xHalfWidth, yHalfWidth)).cast();
	}
}