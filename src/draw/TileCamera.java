package draw;

import geom.d1.*;
import geom.f1.*;

public interface TileCamera
{
	void setXShift(double xShift);

	void setYShift(double yShift);

	int getRange();

	TileLayout layout();

	DoubleTile clickLocation(double x, double y);

	Tile mid(TileLayout layout);
}