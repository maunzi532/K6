package draw;

import geom.XPoint;

public interface VisualGUI
{
	XPoint clickLocation(double x, double y);

	boolean inside(double x, double y);

	void draw();
}