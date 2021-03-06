package geom.camera;

public class XDimension
{
	private double xHW;
	private double yHW;
	private double scaleHW;

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

	public double scaleHW()
	{
		return scaleHW;
	}

	public final void setxW(double xW)
	{
		xHW = xW / 2.0;
		updateScale();
	}

	public final void setyW(double yW)
	{
		yHW = yW / 2.0;
		updateScale();
	}

	private void updateScale()
	{
		scaleHW = Math.min(xHW(), yHW() * 1.5);
	}
}