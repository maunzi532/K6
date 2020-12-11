package geom.layout;

import geom.dtile.*;
import geom.tile.*;

public interface TileLayout
{
	PointD size();

	PointD tileToPixel(Tile t1);

	PointD tileToPixel(DoubleTile t1);

	DoubleTile pixelToTile(PointD p1, DoubleType y2);

	PointD cornerOffset(int corner);

	default PointD cornerOffset(int corner, double distanceMultiplier)
	{
		PointD cornerOffset = cornerOffset(corner);
		return new PointD(cornerOffset.v()[0] * distanceMultiplier, cornerOffset.v()[1] * distanceMultiplier);
	}

	int directionCount();

	default double[][] tileCorners(Tile t1)
	{
		double[][] corners = new double[2][directionCount()];
		PointD center = tileToPixel(t1);
		for(int i = 0; i < directionCount(); i++)
		{
			PointD corner = center.add(cornerOffset(i));
			corners[0][i] = corner.v()[0];
			corners[1][i] = corner.v()[1];
		}
		return corners;
	}

	default double[][] tileCorners(Tile t1, double distanceMultiplier)
	{
		double[][] corners = new double[2][directionCount()];
		PointD center = tileToPixel(t1);
		for(int i = 0; i < directionCount(); i++)
		{
			PointD corner = center.add(cornerOffset(i, distanceMultiplier));
			corners[0][i] = corner.v()[0];
			corners[1][i] = corner.v()[1];
		}
		return corners;
	}

	default double[][] polygonCorners(DoubleTile... tiles)
	{
		double[][] corners = new double[2][tiles.length];
		for(int i = 0; i < tiles.length; i++)
		{
			PointD point = tileToPixel(tiles[i]);
			corners[0][i] = point.v()[0];
			corners[1][i] = point.v()[1];
		}
		return corners;
	}

	PointD imageOffset();
}