package draw;

import geom.*;
import geom.d1.*;
import geom.f1.*;
import gui.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.*;

public class VisualGUIQuad implements VisualGUI
{
	private static final double TEXT_END = 1.35;
	private static final double IMAGE_END = 1.7;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile LU;
	private final DoubleTile RL;

	private final DoubleType y2;
	private final GraphicsContext gd;
	private final QuadCamera camera;
	private final XStateControl stateControl;

	public VisualGUIQuad(DoubleType y2, GraphicsContext gd, double xHalfWidth, double yHalfWidth,
			XStateControl stateControl)
	{
		this.y2 = y2;
		this.gd = gd;
		camera = new QuadCamera(y2, xHalfWidth, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 0,  0);
		this.stateControl = stateControl;
		LU = this.y2.create(new double[]{-3d / 4d, -3d / 4d});
		RL = this.y2.create(new double[]{-1d / 4d, -1d / 4d});
	}

	private DoubleTile rl(XGUI xgui)
	{
		return y2.add(y2.create(new double[]{xgui.xw(), xgui.yw()}), RL);
	}

	@Override
	public Tile clickLocation(double x, double y)
	{
		return y2.cast(camera.clickLocation(x, y));
	}

	@Override
	public boolean inside(double x, double y)
	{
		return inside(camera.clickLocation(x, y));
	}

	public boolean inside(DoubleTile h1)
	{
		XGUI xgui = stateControl.getXgui();
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		DoubleTile rl = rl(xgui);
		return h1.v[0] >= LU.v[0] && h1.v[0] <= rl.v[0] && h1.v[1] >= LU.v[1] && h1.v[1] <= rl.v[1];
	}

	@Override
	public void draw()
	{
		XGUI xgui = stateControl.getXgui();
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return;
		camera.xShift = (xgui.xw() - 1) * QuadLayout2.DQ2;
		camera.yShift = (xgui.yw() - 1) * QuadLayout2.DQ2;
		TileLayout layout = camera.layout();
		gd.setFill(xgui.background());
		Color bg2 = xgui.background().brighter();
		PointD p0 = layout.tileToPixel(LU);
		PointD p1 = layout.tileToPixel(rl(xgui));
		gd.fillRect(p0.v[0], p0.v[1], p1.v[0] - p0.v[0], p1.v[1] - p0.v[1]);
		GuiTile[][] guiTiles = xgui.getTiles();
		for(int ix = 0; ix < xgui.xw(); ix++)
		{
			for(int iy = 0; iy < xgui.yw(); iy++)
			{
				drawQuad(layout, y2.y1().create2(ix, iy), guiTiles[ix][iy], xgui.getTargeted().contains(ix, iy), bg2);
			}
		}
	}

	public void drawQuad(TileLayout layout, Tile h1, GuiTile guiTile, boolean targeted, Color bg2)
	{
		if(guiTile.color != null)
		{
			double[][] points = layout.tileCorners(h1);
			gd.setFill(targeted ? guiTile.color.brighter() : guiTile.color);
			gd.fillPolygon(points[0], points[1], 4);
		}
		else if(targeted)
		{
			double[][] points = layout.tileCorners(h1);
			gd.setFill(bg2);
			gd.fillPolygon(points[0], points[1], 4);
		}
		if(guiTile.image != null)
		{
			PointD midPoint = layout.tileToPixel(h1);
			gd.drawImage(guiTile.image, midPoint.v[0] - layout.size().v[0], midPoint.v[1] - layout.size().v[1],
					layout.size().v[0] * IMAGE_END, layout.size().v[1] * IMAGE_END);
		}
		if(guiTile.text != null)
		{
			PointD midPoint = layout.tileToPixel(y2.add(y2.fromTile(h1), y2.create(new double[]{guiTile.l * -0.5, guiTile.u * -0.5})));
			PointD rEnd = layout.tileToPixel(h1);
			gd.setFont(new Font(layout.size().v[1] * FONT_SIZE));
			if(guiTile.image != null)
			{
				gd.setStroke(Color.WHITE);
				gd.strokeText(guiTile.text, midPoint.v[0], midPoint.v[1], rEnd.v[0] - midPoint.v[0] + layout.size().v[0] * TEXT_END);
			}
			gd.setFill(Color.BLACK);
			gd.fillText(guiTile.text, midPoint.v[0], midPoint.v[1], rEnd.v[0] - midPoint.v[0] + layout.size().v[0] * TEXT_END);
		}
	}
}