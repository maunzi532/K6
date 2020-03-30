package vis.gui;

import geom.*;
import geom.advtile.*;
import logic.gui.*;
import vis.*;

public final class VisualGUIQuad extends VisualGUI
{
	private static final double TEXT_END = 1.35;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile lu;
	private final DoubleTile rl;

	public VisualGUIQuad(XGraphics graphics, QuadCamera camera)
	{
		super(graphics);
		DoubleType y2 = camera.getDoubleType();
		lu = y2.createD(-3d / 4d, -3d / 4d);
		rl = y2.createD(-1d / 4d, -1d / 4d);
	}

	private DoubleTile rlLoc(DoubleType y2, XGUIState xgui)
	{
		return y2.add(y2.createD(xgui.xw(), xgui.yw()), rl);
	}

	@Override
	public boolean inside(TileCamera camera, DoubleTile h1, XGUIState xgui)
	{
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		DoubleTile rlLoc = rlLoc(camera.getDoubleType(), xgui);
		return h1.v()[0] >= lu.v()[0] && h1.v()[0] <= rlLoc.v()[0] && h1.v()[1] >= lu.v()[1] && h1.v()[1] <= rlLoc.v()[1];
	}

	@Override
	public void locateAndDraw(TileCamera camera, XGUIState xgui, Scheme scheme)
	{
		double cxs = (xgui.xw() - 1) * QuadLayout.DQ2;
		double cys = (xgui.yw() - 1) * QuadLayout.DQ2;
		drawGUI(camera, xgui, scheme, cxs, cys, lu, rlLoc(camera.getDoubleType(), xgui), QuadLayout.DQ2, FONT_SIZE, TEXT_END);
	}
}