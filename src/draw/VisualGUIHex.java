package draw;

import geom.*;
import geom.hex.*;
import gui.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.XStateControl;

public class VisualGUIHex implements VisualGUI
{
	private static final double TEXT_END = 1.4;
	private static final double IMAGE_END = 1.8;
	private static final double FONT_SIZE = 0.5;
	private static final DoubleHex LU = new DoubleHex(-1d / 6d, 5d / 6d, -4d / 6d);
	private static final DoubleHex RLE = new DoubleHex(1d / 6d, -5d / 6d, 4d / 6d);
	private static final DoubleHex RLS = new DoubleHex(4d / 6d, -8d / 6d, 4d / 6d);

	private final GraphicsContext gd;
	private final HexCamera camera;
	private final XStateControl stateControl;

	public VisualGUIHex(GraphicsContext gd, double xHalfWidth, double yHalfWidth, XStateControl stateControl)
	{
		this.gd = gd;
		camera = new HexCamera(xHalfWidth, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 0,  0, HexMatrix.LP);
		this.stateControl = stateControl;
	}

	private static DoubleHex rl(XGUI xgui)
	{
		return new DoubleHex(new XPoint(xgui.xw() - 1, xgui.yw() - 1).toHex()).add((xgui.yw() - 2) % 2 == 1 ? RLS : RLE);
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

	public boolean inside(DoubleHex h1)
	{
		XGUI xgui = stateControl.getXgui();
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		double xc = h1.v[0] - h1.v[1];
		double yc = h1.v[2];
		DoubleHex rl = rl(xgui);
		return xc >= LU.v[0] - LU.v[1] && xc <= rl.v[0] - rl.v[1] && yc >= LU.v[2] && yc <= rl.v[2];
	}

	@Override
	public void draw()
	{
		XGUI xgui = stateControl.getXgui();
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return;
		camera.xShift = (xgui.xw() - (xgui.yw() > 1 ? 0.5 : 1d)) * HexMatrix.Q3 / 2d;
		camera.yShift = (xgui.yw() - 1) * 1.5 / 2d;
		HexLayout layout = camera.layout();
		gd.setFill(xgui.background());
		Color bg2 = xgui.background().brighter();
		PointD p0 = layout.hexToPixel(LU);
		PointD p1 = layout.hexToPixel(rl(xgui));
		gd.fillRect(p0.v[0], p0.v[1], p1.v[0] - p0.v[0], p1.v[1] - p0.v[1]);
		GuiTile[][] guiTiles = xgui.getTiles();
		for(int ix = 0; ix < xgui.xw(); ix++)
		{
			for(int iy = 0; iy < xgui.yw(); iy++)
			{
				drawHex(layout, new XPoint(ix, iy).toHex(), guiTiles[ix][iy], xgui.getTargeted().contains(ix, iy), bg2);
			}
		}
	}

	public void drawHex(HexLayout layout, Hex h1, GuiTile guiTile, boolean targeted, Color bg2)
	{
		if(guiTile.color != null)
		{
			double[][] points = layout.hexCorners(h1);
			gd.setFill(targeted ? guiTile.color.brighter() : guiTile.color);
			gd.fillPolygon(points[0], points[1], 6);
		}
		else if(targeted)
		{
			double[][] points = layout.hexCorners(h1);
			gd.setFill(bg2);
			gd.fillPolygon(points[0], points[1], 6);
		}
		if(guiTile.image != null)
		{
			PointD midPoint = layout.hexToPixel(h1);
			gd.drawImage(guiTile.image, midPoint.v[0] - layout.size.v[0], midPoint.v[1] - layout.size.v[1],
					layout.size.v[0] * IMAGE_END, layout.size.v[1] * IMAGE_END);
		}
		if(guiTile.text != null)
		{
			PointD midPoint = layout.hexToPixel(new DoubleHex(h1).add(new DoubleHex(guiTile.l * -0.5 + guiTile.u * 0.25,
					guiTile.l * 0.5 + guiTile.u * 0.25, guiTile.u * -0.5)));
			PointD rEnd = layout.hexToPixel(h1);
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