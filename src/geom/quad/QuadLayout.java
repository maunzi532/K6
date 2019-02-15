package geom.quad;

import geom.PointD;

public class QuadLayout
{
	public static final double Q2 = Math.sqrt(2);
	public static final double DQ2 = Math.sqrt(2) / 2;

	public final PointD size;
	public final PointD origin;

	public QuadLayout(PointD size, PointD origin)
	{
		this.size = size;
		this.origin = origin;
	}

	public PointD quadToPixel(Quad h1)
	{
		double x0 = h1.v[0] * Q2;
		double y0 = h1.v[1] * Q2;
		return new PointD(x0 * size.v[0] + origin.v[0], y0 * size.v[1] + origin.v[1]);
	}

	public PointD quadToPixel(DoubleQuad h1)
	{
		double x0 = h1.v[0] * Q2;
		double y0 = h1.v[1] * Q2;
		return new PointD(x0 * size.v[0] + origin.v[0], y0 * size.v[1] + origin.v[1]);
	}

	public DoubleQuad pixelToQuad(PointD p1)
	{
		PointD p2 = new PointD((p1.v[0] - origin.v[0]) / size.v[0], (p1.v[1] - origin.v[1]) / size.v[1]);
		double x = p2.v[0] * DQ2;
		double y = p2.v[1] * DQ2;
		return new DoubleQuad(x, y);
	}

	public PointD cornerOffset(int corner)
	{
		double angle = Math.PI * (0.5 + corner) / 2f;
		return new PointD(size.v[0] * Math.cos(angle), size.v[1] * Math.sin(angle));
	}

	public PointD[] quadCornersPointD(Quad h1)
	{
		PointD[] corners = new PointD[4];
		PointD center = quadToPixel(h1);
		for(int i = 0; i < 4; i++)
		{
			corners[i] = center.plus(cornerOffset(i));
		}
		return corners;
	}

	public double[][] quadCorners(Quad h1)
	{
		double[][] corners = new double[2][4];
		PointD center = quadToPixel(h1);
		for(int i = 0; i < 4; i++)
		{
			PointD corner = center.plus(cornerOffset(i));
			corners[0][i] = corner.v[0];
			corners[1][i] = corner.v[1];
		}
		return corners;
	}

	public double[][] quadCorners(DoubleQuad h1)
	{
		double[][] corners = new double[2][4];
		PointD center = quadToPixel(h1);
		for(int i = 0; i < 4; i++)
		{
			PointD corner = center.plus(cornerOffset(i));
			corners[0][i] = corner.v[0];
			corners[1][i] = corner.v[1];
		}
		return corners;
	}

	public double[][] multiQuadToPixel(Quad... h1)
	{
		double[][] corners = new double[2][h1.length];
		for(int i = 0; i < h1.length; i++)
		{
			PointD point = quadToPixel(h1[i]);
			corners[0][i] = point.v[0];
			corners[1][i] = point.v[1];
		}
		return corners;
	}

	public double[][] multiQuadToPixel(DoubleQuad... h1)
	{
		double[][] corners = new double[2][h1.length];
		for(int i = 0; i < h1.length; i++)
		{
			PointD point = quadToPixel(h1[i]);
			corners[0][i] = point.v[0];
			corners[1][i] = point.v[1];
		}
		return corners;
	}
}