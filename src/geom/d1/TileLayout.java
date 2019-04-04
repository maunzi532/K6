package geom.d1;

import geom.*;
import geom.f1.*;

public interface TileLayout
{
	PointD size();

	PointD tileToPixel(Tile t1);

	PointD tileToPixel(DoubleTile t1);

	DoubleTile pixelToTile(PointD p1, DoubleType y2);

	PointD cornerOffset(int corner);

	int directionCount();

	default double[][] tileCorners(Tile t1)
	{
		double[][] corners = new double[2][directionCount()];
		PointD center = tileToPixel(t1);
		for(int i = 0; i < directionCount(); i++)
		{
			PointD corner = center.plus(cornerOffset(i));
			corners[0][i] = corner.v[0];
			corners[1][i] = corner.v[1];
		}
		return corners;
	}

	default double[][] polygonCorners(DoubleTile... tiles)
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