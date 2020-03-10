package levelMap;

import geom.f1.*;
import javafx.scene.paint.*;

public record VisMark(Tile location, Color color, double midDistance)
{
	public static final double d1 = 0.95;
	public static final double d2 = 0.9;
	public static final double d3 = 1;
}