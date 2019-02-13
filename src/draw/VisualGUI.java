package draw;

import building.*;
import hex.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.*;
import logic.gui.*;

public class VisualGUI
{
	private static final DoubleHex LU = new DoubleHex(-1d / 6d, 5d / 6d, -4d / 6d);
	private static final DoubleHex RLE = new DoubleHex(1d / 6d, -5d / 6d, 4d / 6d);
	private static final DoubleHex RLS = new DoubleHex(4d / 6d, -8d / 6d, 4d / 6d);
	private final GraphicsContext gd;
	public final XCamera camera;
	private final XStateControl stateControl;

	public VisualGUI(GraphicsContext gd, double xHalfWidth, double yHalfWidth, XStateControl stateControl)
	{
		this.gd = gd;
		camera = new XCamera(xHalfWidth, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 0,  0, MatrixH.LP);
		this.stateControl = stateControl;
	}

	private static DoubleHex rl(XGUI xgui)
	{
		return new DoubleHex(new OffsetHex(xgui.xw() - 1, xgui.yw() - 1).toHex()).add((xgui.yw() - 2) % 2 == 1 ? RLS : RLE);
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

	public void draw()
	{
		XGUI xgui = stateControl.getXgui();
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return;
		camera.xShift = (xgui.xw() - (xgui.yw() > 1 ? 0.5 : 1d)) * MatrixH.Q3 / 2d;
		camera.yShift = (xgui.yw() - 1) * 1.5 / 2d;
		LayoutH layout = camera.layout();
		gd.setFill(Color.RED);
		PointD p0 = layout.hexToPixel(LU);
		PointD p1 = layout.hexToPixel(rl(xgui));
		gd.fillRect(p0.v[0], p0.v[1], p1.v[0] - p0.v[0], p1.v[1] - p0.v[1]);
		GuiTile[][] guiTiles = xgui.getTiles();
		for(int i = 0; i < xgui.xw(); i++)
		{
			for(int j = 0; j < xgui.yw(); j++)
			{
				drawHex(layout, new OffsetHex(i, j).toHex(), guiTiles[i][j]);
			}
		}
	}

	public void drawHex(LayoutH layout, Hex h1, GuiTile guiTile)
	{
		if(guiTile == null)
			return;
		if(guiTile.color != null)
		{
			double[][] points = layout.hexCorners(h1);
			gd.setFill(Color.ORANGERED);
			gd.fillPolygon(points[0], points[1], 6);
		}
		if(guiTile.image != null)
		{
			PointD midPoint = layout.hexToPixel(h1);
			gd.drawImage(Building.IMAGE, midPoint.v[0] - layout.size.v[0], midPoint.v[1] - layout.size.v[1],
					layout.size.v[0] * 2, layout.size.v[1] * 2);
		}
		if(guiTile.text != null)
		{
			PointD midPoint = layout.hexToPixel(h1);
			gd.setFill(Color.BLACK);
			gd.setFont(new Font(layout.size.v[1] * 0.5));
			gd.fillText(guiTile.text, midPoint.v[0] - layout.size.v[0] * 0.7, midPoint.v[1] + layout.size.v[1] * 0.2, layout.size.v[0] * 1.4);
		}
	}
}