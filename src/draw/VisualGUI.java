package draw;

import geom.*;
import geom.d1.*;
import geom.f1.*;
import javafx.scene.canvas.*;
import logic.*;

public abstract class VisualGUI
{
	public final DoubleType y2;
	protected final GraphicsContext gd;
	protected final TileCamera camera;
	protected final XStateControl stateControl;

	public VisualGUI(GraphicsContext gd, TileCamera camera, XStateControl stateControl)
	{
		this.gd = gd;
		this.camera = camera;
		y2 = camera.getDoubleType();
		this.stateControl = stateControl;
	}

	public Tile clickLocation(double x, double y)
	{
		return y2.cast(camera.clickLocation(x, y));
	}

	public boolean inside(double x, double y)
	{
		return inside(camera.clickLocation(x, y));
	}

	public abstract boolean inside(DoubleTile h1);

	public abstract void draw();
}