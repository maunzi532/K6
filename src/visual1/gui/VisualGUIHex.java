package visual1.gui;

import geom.*;
import geom.d1.*;
import logic.gui.*;
import visual1.*;

public class VisualGUIHex extends VisualGUI
{
	private static final double TEXT_END = 1.4;
	private static final double IMAGE_END = 1;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile LU;
	private final DoubleTile RLE;
	private final DoubleTile RLS;

	public VisualGUIHex(XGraphics graphics, HexCamera camera)
	{
		super(graphics);
		DoubleType y2 = camera.getDoubleType();
		LU = y2.createD(-1d / 6d, 5d / 6d, -4d / 6d);
		RLE = y2.createD(1d / 6d, -5d / 6d, 4d / 6d);
		RLS = y2.createD(4d / 6d, -8d / 6d, 4d / 6d);
	}

	private DoubleTile rl(DoubleType y2, XGUIState xgui)
	{
		return y2.add(y2.fromTile(y2.fromOffset(xgui.xw() - 1, xgui.yw() - 1)), (xgui.yw() - 2) % 2 == 1 ? RLS : RLE);
	}

	@Override
	public boolean inside(TileCamera camera, DoubleTile h1, XGUIState xgui)
	{
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		double xc = h1.v()[0] - h1.v()[1];
		double yc = h1.v()[2];
		DoubleTile rl = rl(camera.getDoubleType(), xgui);
		return xc >= LU.v()[0] - LU.v()[1] && xc <= rl.v()[0] - rl.v()[1] && yc >= LU.v()[2] && yc <= rl.v()[2];
	}

	@Override
	public void locateAndDraw(TileCamera camera, XGUIState xgui, Scheme scheme)
	{
		double cxs = xgui.xw() - (xgui.yw() > 1 ? 0.5 : 1d) * HexMatrix.Q3 / 2d;
		double cys = (xgui.yw() - 1) * 1.5 / 2d;
		drawGUI(camera, xgui, scheme, cxs, cys, LU, rl(camera.getDoubleType(), xgui), IMAGE_END, FONT_SIZE, TEXT_END);
	}
}