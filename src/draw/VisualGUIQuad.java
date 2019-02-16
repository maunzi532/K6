package draw;

import geom.*;
import geom.quad.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.XStateControl;
import gui.*;

public class VisualGUIQuad implements VisualGUI
{
	private static final double TEXT_END = 1.35;
	private static final double IMAGE_END = 1.7;
	private static final double FONT_SIZE = 0.5;
	private static final DoubleQuad LU = new DoubleQuad(-3d / 4d, -3d / 4d);
	private static final DoubleQuad RL = new DoubleQuad(-1d / 4d, -1d / 4d);

	private final GraphicsContext gd;
	private final QuadCamera camera;
	private final XStateControl stateControl;

	public VisualGUIQuad(GraphicsContext gd, double xHalfWidth, double yHalfWidth, XStateControl stateControl)
	{
		this.gd = gd;
		camera = new QuadCamera(xHalfWidth, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 0,  0);
		this.stateControl = stateControl;
	}

	private static DoubleQuad rl(XGUI xgui)
	{
		return new DoubleQuad(new Quad(xgui.xw(), xgui.yw())).add(RL);
	}

	@Override
	public XPoint clickLocation(double x, double y)
	{
		return new XPoint(camera.clickLocation(x, y).cast());
	}

	@Override
	public boolean inside(double x, double y)
	{
		return inside(camera.clickLocation(x, y));
	}

	public boolean inside(DoubleQuad h1)
	{
		XGUI xgui = stateControl.getXgui();
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		DoubleQuad rl = rl(xgui);
		return h1.v[0] >= LU.v[0] && h1.v[0] <= rl.v[0] && h1.v[1] >= LU.v[1] && h1.v[1] <= rl.v[1];
	}

	@Override
	public void draw()
	{
		XGUI xgui = stateControl.getXgui();
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return;
		camera.xShift = (xgui.xw() - 1) * QuadLayout.DQ2;
		camera.yShift = (xgui.yw() - 1) * QuadLayout.DQ2;
		QuadLayout layout = camera.layout();
		gd.setFill(xgui.background());
		Color bg2 = xgui.background().brighter();
		PointD p0 = layout.quadToPixel(LU);
		PointD p1 = layout.quadToPixel(rl(xgui));
		gd.fillRect(p0.v[0], p0.v[1], p1.v[0] - p0.v[0], p1.v[1] - p0.v[1]);
		GuiTile[][] guiTiles = xgui.getTiles();
		for(int ix = 0; ix < xgui.xw(); ix++)
		{
			for(int iy = 0; iy < xgui.yw(); iy++)
			{
				drawQuad(layout, new Quad(ix, iy), guiTiles[ix][iy], xgui.getTargeted().contains(ix, iy), bg2);
			}
		}
	}

	public void drawQuad(QuadLayout layout, Quad h1, GuiTile guiTile, boolean targeted, Color bg2)
	{
		if(guiTile.color != null)
		{
			double[][] points = layout.quadCorners(h1);
			gd.setFill(targeted ? guiTile.color.brighter() : guiTile.color);
			gd.fillPolygon(points[0], points[1], 4);
		}
		else if(targeted)
		{
			double[][] points = layout.quadCorners(h1);
			gd.setFill(bg2);
			gd.fillPolygon(points[0], points[1], 4);
		}
		if(guiTile.image != null)
		{
			PointD midPoint = layout.quadToPixel(h1);
			gd.drawImage(guiTile.image, midPoint.v[0] - layout.size.v[0], midPoint.v[1] - layout.size.v[1],
					layout.size.v[0] * IMAGE_END, layout.size.v[1] * IMAGE_END);
		}
		if(guiTile.text != null)
		{
			PointD midPoint = layout.quadToPixel(new DoubleQuad(h1).add(new DoubleQuad(guiTile.l * -0.5, guiTile.u * -0.5)));
			PointD rEnd = layout.quadToPixel(h1);
			gd.setFont(new Font(layout.size.v[1] * FONT_SIZE));
			if(guiTile.image != null)
			{
				gd.setStroke(Color.WHITE);
				gd.strokeText(guiTile.text, midPoint.v[0], midPoint.v[1], rEnd.v[0] - midPoint.v[0] + layout.size.v[0] * TEXT_END);
			}
			gd.setFill(Color.BLACK);
			gd.fillText(guiTile.text, midPoint.v[0], midPoint.v[1], rEnd.v[0] - midPoint.v[0] + layout.size.v[0] * TEXT_END);
		}
	}
}