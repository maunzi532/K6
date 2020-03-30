package geom.advtile;

import geom.tile.*;

public interface DoubleType extends TileType
{
	static double lerp(double a1, double a2, double t)
	{
		return a1 * (1.0 - t) + a2 * t;
	}

	DoubleTile createD(double... v);

	DoubleTile fromTile(Tile t1);

	Tile cast(DoubleTile t1);

	DoubleTile add(DoubleTile t1, DoubleTile t2);

	DoubleTile subtract(DoubleTile t1, DoubleTile minus);

	DoubleTile multiply(DoubleTile t1, double scalar);

	default DoubleTile normalize(Tile t1)
	{
		return normalize(fromTile(t1));
	}

	DoubleTile normalize(DoubleTile t1);

	DoubleTile rotateR2(DoubleTile t1, boolean inverse);

	DoubleTile upwards();

	DoubleTile tileLerp(DoubleTile t1, DoubleTile t2, double t);

	DoubleTile fromOffsetD(double x, double y);
}