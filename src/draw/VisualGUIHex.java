package draw;

import geom.*;
import geom.d1.*;
import gui.*;
import javafx.scene.canvas.*;
import logic.*;

public class VisualGUIHex extends VisualGUI
{
	private static final double TEXT_END = 1.4;
	private static final double IMAGE_END = 1;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile LU;
	private final DoubleTile RLE;
	private final DoubleTile RLS;

	public VisualGUIHex(GraphicsContext gd, double xHalfWidth, double yHalfWidth,
			XStateControl stateControl)
	{
		super(gd, new HexCamera(xHalfWidth, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 0,  0, HexMatrix.LP), stateControl);
		LU = y2.createD(-1d / 6d, 5d / 6d, -4d / 6d);
		RLE = y2.createD(1d / 6d, -5d / 6d, 4d / 6d);
		RLS = y2.createD(4d / 6d, -8d / 6d, 4d / 6d);
	}

	private DoubleTile rl(XGUI xgui)
	{
		return y2.add(y2.fromTile(y2.fromOffset(xgui.xw() - 1, xgui.yw() - 1)), (xgui.yw() - 2) % 2 == 1 ? RLS : RLE);
	}

	@Override
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
		double cxs = xgui.xw() - (xgui.yw() > 1 ? 0.5 : 1d) * HexMatrix.Q3 / 2d;
		double cys = (xgui.yw() - 1) * 1.5 / 2d;
		draw1(xgui, cxs, cys, LU, rl(xgui), IMAGE_END, FONT_SIZE, TEXT_END);
	}
}