package geom.d1;

import geom.*;
import geom.f1.*;
import geom.hex.*;

public class HexLayout2 implements TileLayout
{
	public final HexMatrix mat;
	public final PointD size;
	public final PointD origin;
	//public final PointD

	public HexLayout2(HexMatrix mat, PointD size, PointD origin)
	{
		this.mat = mat;
		this.size = size;
		this.origin = origin;
	}

	@Override
	public PointD size()
	{
		return size;
	}

	@Override
	public PointD tileToPixel(Tile t1)
	{
		double x0 = mat.matrix[0][0] * t1.v[0] + mat.matrix[0][1] * t1.v[2];
		double y0 = mat.matrix[0][2] * t1.v[0] + mat.matrix[0][3] * t1.v[2];
		return new PointD(x0 * size.v[0] + origin.v[0], y0 * size.v[1] + origin.v[1]);
	}

	@Override
	public PointD tileToPixel(DoubleTile t1)
	{
		double x0 = mat.matrix[0][0] * t1.v[0] + mat.matrix[0][1] * t1.v[2];
		double y0 = mat.matrix[0][2] * t1.v[0] + mat.matrix[0][3] * t1.v[2];
		return new PointD(x0 * size.v[0] + origin.v[0], y0 * size.v[1] + origin.v[1]);
	}

	@Override
	public DoubleTile pixelToTile(PointD p1, DoubleType y2)
	{
		PointD p2 = new PointD((p1.v[0] - origin.v[0]) / size.v[0], (p1.v[1] - origin.v[1]) / size.v[1]);
		double x = mat.matrix[1][0] * p2.v[0] + mat.matrix[1][1] * p2.v[1];
		double z = mat.matrix[1][2] * p2.v[0] + mat.matrix[1][3] * p2.v[1];
		return y2.create(new double[]{x, -x - z, z});
	}

	@Override
	public PointD cornerOffset(int corner)
	{
		double angle = Math.PI * (mat.startAngle + corner) / -3f;
		return new PointD(size.v[0] * Math.cos(angle), size.v[1] * Math.sin(angle));
	}

	@Override
	public PointD cornerOffset(int corner, double m)
	{
		double angle = Math.PI * (mat.startAngle + corner) / -3f;
		return new PointD(size.v[0] * Math.cos(angle) * m, size.v[1] * Math.sin(angle) * m);
	}

	@Override
	public PointD[] tileCornersPointD(Tile t1)
	{
		PointD[] corners = new PointD[6];
		PointD center = tileToPixel(t1);
		for(int i = 0; i < 6; i++)
		{
			corners[i] = center.plus(cornerOffset(i));
		}
		return corners;
	}

	@Override
	public double[][] tileCorners(Tile t1)
	{
		double[][] corners = new double[2][6];
		PointD center = tileToPixel(t1);
		for(int i = 0; i < 6; i++)
		{
			PointD corner = center.plus(cornerOffset(i));
			corners[0][i] = corner.v[0];
			corners[1][i] = corner.v[1];
		}
		return corners;
	}

	@Override
	public double[][] tileCorners(Tile t1, double m)
	{
		double[][] corners = new double[2][6];
		PointD center = tileToPixel(t1);
		for(int i = 0; i < 6; i++)
		{
			PointD corner = center.plus(cornerOffset(i, m));
			corners[0][i] = corner.v[0];
			corners[1][i] = corner.v[1];
		}
		return corners;
	}

	@Override
	public double[][] tileCorners(DoubleTile t1)
	{
		double[][] corners = new double[2][6];
		PointD center = tileToPixel(t1);
		for(int i = 0; i < 6; i++)
		{
			PointD corner = center.plus(cornerOffset(i));
			corners[0][i] = corner.v[0];
			corners[1][i] = corner.v[1];
		}
		return corners;
	}

	@Override
	public double[][] multiTileToPixel(Tile... tiles)
	{
		double[][] corners = new double[2][tiles.length];
		for(int i = 0; i < tiles.length; i++)
		{
			PointD point = tileToPixel(tiles[i]);
			corners[0][i] = point.v[0];
			corners[1][i] = point.v[1];
		}
		return corners;
	}

	@Override
	public double[][] multiTileToPixel(DoubleTile... tiles)
	{
		double[][] corners = new double[2][tiles.length];
		for(int i = 0; i < tiles.length; i++)
		{
			PointD point = tileToPixel(tiles[i]);
			corners[0][i] = point.v[0];
			corners[1][i] = point.v[1];
		}
		return corners;
	}
}