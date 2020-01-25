package visual.gui;

import geom.*;
import geom.d1.*;
import logic.gui.*;
import visual.*;

public class VisualGUIQuad extends VisualGUI
{
	private static final double TEXT_END = 1.35;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile LU;
	private final DoubleTile RL;

	public VisualGUIQuad(XGraphics graphics, QuadCamera camera)
	{
		super(graphics.gd(), camera);
		LU = this.y2.createD(-3d / 4d, -3d / 4d);
		RL = this.y2.createD(-1d / 4d, -1d / 4d);
	}

	private DoubleTile rl(XGUIState xgui)
	{
		return y2.add(y2.createD(xgui.xw(), xgui.yw()), RL);
	}

	@Override
	public boolean inside(DoubleTile h1, XGUIState xgui)
	{
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		DoubleTile rl = rl(xgui);
		return h1.v()[0] >= LU.v()[0] && h1.v()[0] <= rl.v()[0] && h1.v()[1] >= LU.v()[1] && h1.v()[1] <= rl.v()[1];
	}

	@Override
	public void draw(XGUIState xgui)
	{
		double cxs = (xgui.xw() - 1) * QuadLayout.DQ2;
		double cys = (xgui.yw() - 1) * QuadLayout.DQ2;
		draw1(xgui, cxs, cys, LU, rl(xgui), QuadLayout.DQ2, FONT_SIZE, TEXT_END);
	}
}