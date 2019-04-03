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

	PointD cornerOffset(int corner, double m);

	PointD[] tileCornersPointD(Tile t1);

	double[][] tileCorners(Tile t1);

	double[][] tileCorners(Tile t1, double m);

	double[][] tileCorners(DoubleTile t1);

	double[][] multiTileToPixel(Tile... tiles);

	double[][] multiTileToPixel(DoubleTile... tiles);
}