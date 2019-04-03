package draw;

import geom.f1.*;

public interface VisualGUI
{
	Tile clickLocation(double x, double y);

	boolean inside(double x, double y);

	void draw();
}