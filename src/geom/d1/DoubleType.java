package geom.d1;

import geom.f1.*;

public interface DoubleType extends TileType
{
	static double lerp(double a, double b, double t)
	{
		return a * (1 - t) + b * t;
	}

	DoubleTile createD(double... v);

	DoubleTile fromTile(Tile t1);

	Tile cast(DoubleTile t1);

	DoubleTile add(DoubleTile t1, DoubleTile t2);

	DoubleTile subtract(DoubleTile t1, DoubleTile minus);

	DoubleTile multiply(DoubleTile t1, double scalar);

	double length(DoubleTile t1);

	default DoubleTile normalize(Tile t1)
	{
		return normalize(fromTile(t1));
	}

	DoubleTile normalize(DoubleTile t1);

	DoubleTile rotate(DoubleTile t1, boolean inverse);

	DoubleTile rotateR(DoubleTile t1, boolean inverse);

	DoubleTile tileLerp(DoubleTile t1, DoubleTile t2, double t);
}