package geom;

public class XDimension
{
	private double xHW;
	private double yHW;

	public XDimension(double xW, double yW)
	{
		setxW(xW);
		setyW(yW);
	}

	public double xHW()
	{
		return xHW;
	}

	public double yHW()
	{
		return yHW;
	}

	public void setxW(double xW)
	{
		xHW = xW / 2f;
	}

	public void setyW(double yW)
	{
		yHW = yW / 2f;
	}
}