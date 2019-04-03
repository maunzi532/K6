package draw;

import geom.*;
import geom.d1.*;
import geom.f1.*;
import geom.hex.*;
import gui.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.*;

public class VisualGUIHex implements VisualGUI
{
	private static final double TEXT_END = 1.4;
	private static final double IMAGE_END = 1.8;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile LU;
	private final DoubleTile RLE;
	private final DoubleTile RLS;

	private final DoubleType y2;
	private final GraphicsContext gd;
	private final TileCamera camera;
	private final XStateControl stateControl;

	public VisualGUIHex(DoubleType y2, GraphicsContext gd, double xHalfWidth, double yHalfWidth,
			XStateControl stateControl)
	{
		this.y2 = y2;
		this.gd = gd;
		camera = new HexCamera(this.y2, xHalfWidth, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 0,  0, HexMatrix.LP);
		this.stateControl = stateControl;
		LU = y2.create(new double[]{-1d / 6d, 5d / 6d, -4d / 6d});
		RLE = y2.create(new double[]{1d / 6d, -5d / 6d, 4d / 6d});
		RLS = y2.create(new double[]{4d / 6d, -8d / 6d, 4d / 6d});
	}

	private DoubleTile rl(XGUI xgui)
	{
		return y2.add(y2.fromTile(asHex(xgui.xw() - 1, xgui.yw() - 1)), (xgui.yw() - 2) % 2 == 1 ? RLS : RLE);
	}

	private Tile asHex(int n1, int n2)
	{
		return y2.y1().create2(n1 - n2 / 2, n2);
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
		double xc = h1.v[0] - h1.v[1];
		double yc = h1.v[2];
		DoubleTile rl = rl(xgui);
		return xc >= LU.v[0] - LU.v[1] && xc <= rl.v[0] - rl.v[1] && yc >= LU.v[2] && yc <= rl.v[2];
	}

	@Override
	public void draw()
	{
		XGUI xgui = stateControl.getXgui();
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return;
		camera.setXShift(xgui.xw() - (xgui.yw() > 1 ? 0.5 : 1d) * HexMatrix.Q3 / 2d);
		camera.setYShift((xgui.yw() - 1) * 1.5 / 2d);
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
				drawHex(layout, asHex(ix, iy), guiTiles[ix][iy], xgui.getTargeted().contains(ix, iy), bg2);
			}
		}
	}

	public void drawHex(TileLayout layout, Tile t1, GuiTile guiTile, boolean targeted, Color bg2)
	{
		if(guiTile.color != null)
		{
			double[][] points = layout.tileCorners(t1);
			gd.setFill(targeted ? guiTile.color.brighter() : guiTile.color);
			gd.fillPolygon(points[0], points[1], 6);
		}
		else if(targeted)
		{
			double[][] points = layout.tileCorners(t1);
			gd.setFill(bg2);
			gd.fillPolygon(points[0], points[1], 6);
		}
		if(guiTile.image != null)
		{
			PointD midPoint = layout.tileToPixel(t1);
			gd.drawImage(guiTile.image, midPoint.v[0] - layout.size().v[0], midPoint.v[1] - layout.size().v[1],
					layout.size().v[0] * IMAGE_END, layout.size().v[1] * IMAGE_END);
		}
		if(guiTile.text != null)
		{
			PointD midPoint = layout.tileToPixel(y2.add(y2.fromTile(t1), y2.create(new double[]{guiTile.l * -0.5 + guiTile.u * 0.25,
					guiTile.l * 0.5 + guiTile.u * 0.25, guiTile.u * -0.5})));
			PointD rEnd = layout.tileToPixel(t1);
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