package geom.hex;

import geom.PointD;

public class HexLayout
{
	public final HexMatrix mat;
	public final PointD size;
	public final PointD origin;

	public HexLayout(HexMatrix mat, PointD size, PointD origin)
	{
		this.mat = mat;
		this.size = size;
		this.origin = origin;
	}

	public PointD hexToPixel(Hex h1)
	{
		double x0 = mat.matrix[0][0] * h1.v[0] + mat.matrix[0][1] * h1.v[2];
		double y0 = mat.matrix[0][2] * h1.v[0] + mat.matrix[0][3] * h1.v[2];
		return new PointD(x0 * size.v[0] + origin.v[0], y0 * size.v[1] + origin.v[1]);
	}

	public PointD hexToPixel(DoubleHex h1)
	{
		double x0 = mat.matrix[0][0] * h1.v[0] + mat.matrix[0][1] * h1.v[2];
		double y0 = mat.matrix[0][2] * h1.v[0] + mat.matrix[0][3] * h1.v[2];
		return new PointD(x0 * size.v[0] + origin.v[0], y0 * size.v[1] + origin.v[1]);
	}

	public DoubleHex pixelToHex(PointD p1)
	{
		PointD p2 = new PointD((p1.v[0] - origin.v[0]) / size.v[0], (p1.v[1] - origin.v[1]) / size.v[1]);
		double x = mat.matrix[1][0] * p2.v[0] + mat.matrix[1][1] * p2.v[1];
		double z = mat.matrix[1][2] * p2.v[0] + mat.matrix[1][3] * p2.v[1];
		return new DoubleHex(x, -x - z, z);
	}

	public PointD cornerOffset(int corner)
	{
		double angle = Math.PI * (mat.startAngle + corner) / -3f;
		return new PointD(size.v[0] * Math.cos(angle), size.v[1] * Math.sin(angle));
	}

	public PointD[] hexCornersPointD(Hex h1)
	{
		PointD[] corners = new PointD[6];
		PointD center = hexToPixel(h1);
		for(int i = 0; i < 6; i++)
		{
			corners[i] = center.plus(cornerOffset(i));
		}
		return corners;
	}

	public double[][] hexCorners(Hex h1)
	{
		double[][] corners = new double[2][6];
		PointD center = hexToPixel(h1);
		for(int i = 0; i < 6; i++)
		{
			PointD corner = center.plus(cornerOffset(i));
			corners[0][i] = corner.v[0];
			corners[1][i] = corner.v[1];
		}
		return corners;
	}

	public double[][] hexCorners(DoubleHex h1)
	{
		double[][] corners = new double[2][6];
		PointD center = hexToPixel(h1);
		for(int i = 0; i < 6; i++)
		{
			PointD corner = center.plus(cornerOffset(i));
			corners[0][i] = corner.v[0];
			corners[1][i] = corner.v[1];
		}
		return corners;
	}

	public double[][] multiHexToPixel(Hex... h1)
	{
		double[][] corners = new double[2][h1.length];
		for(int i = 0; i < h1.length; i++)
		{
			PointD point = hexToPixel(h1[i]);
			corners[0][i] = point.v[0];
			corners[1][i] = point.v[1];
		}
		return corners;
	}

	public double[][] multiHexToPixel(DoubleHex... h1)
	{
		double[][] corners = new double[2][h1.length];
		for(int i = 0; i < h1.length; i++)
		{
			PointD point = hexToPixel(h1[i]);
			corners[0][i] = point.v[0];
			corners[1][i] = point.v[1];
		}
		return corners;
	}
}