package levelmap;

import geom.tile.*;

public record VisMark(Tile location, String color, double midDistance)
{
	public static final double d1 = 0.95;
	public static final double d2 = 0.9;
	public static final double d3 = 1.0;
}