package vis;

import geom.*;
import javafx.scene.canvas.*;

public final class XGraphics extends XDimension
{
	private GraphicsContext gd;

	public XGraphics(GraphicsContext gd, double xW, double yW)
	{
		super(xW, yW);
		this.gd = gd;
	}

	public GraphicsContext gd()
	{
		return gd;
	}
}