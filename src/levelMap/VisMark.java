package levelMap;

import geom.f1.*;
import javafx.scene.paint.*;

public class VisMark
{
	public static final double d1 = 0.95;
	public static final double d2 = 0.9;
	public static final double d3 = 1;

	private final Tile location;
	private final Color color;
	private final double midDistance;

	public VisMark(Tile location, Color color, double midDistance)
	{
		this.location = location;
		this.color = color;
		this.midDistance = midDistance;
	}

	public Tile getLocation()
	{
		return location;
	}

	public Color getColor()
	{
		return color;
	}

	public double getMidDistance()
	{
		return midDistance;
	}
}