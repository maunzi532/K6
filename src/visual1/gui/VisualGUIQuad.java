package visual1.gui;

import geom.*;
import geom.d1.*;
import logic.gui.*;
import visual1.*;

public class VisualGUIQuad extends VisualGUI
{
	private static final double TEXT_END = 1.35;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile LU;
	private final DoubleTile RL;

	public VisualGUIQuad(XGraphics graphics, QuadCamera camera)
	{
		super(graphics);
		DoubleType y2 = camera.getDoubleType();
		LU = y2.createD(-3d / 4d, -3d / 4d);
		RL = y2.createD(-1d / 4d, -1d / 4d);
	}

	private DoubleTile rl(DoubleType y2, XGUIState xgui)
	{
		return y2.add(y2.createD(xgui.xw(), xgui.yw()), RL);
	}

	@Override
	public boolean inside(TileCamera camera, DoubleTile h1, XGUIState xgui)
	{
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		DoubleTile rl = rl(camera.getDoubleType(), xgui);
		return h1.v()[0] >= LU.v()[0] && h1.v()[0] <= rl.v()[0] && h1.v()[1] >= LU.v()[1] && h1.v()[1] <= rl.v()[1];
	}

	@Override
	public void locateAndDraw(TileCamera camera, XGUIState xgui, Scheme scheme)
	{
		double cxs = (xgui.xw() - 1) * QuadLayout.DQ2;
		double cys = (xgui.yw() - 1) * QuadLayout.DQ2;
		drawGUI(camera, xgui, scheme, cxs, cys, LU, rl(camera.getDoubleType(), xgui), QuadLayout.DQ2, FONT_SIZE, TEXT_END);
	}
}