package vis.gui;

import geom.layout.*;
import geom.camera.*;
import geom.dtile.*;
import gui.*;
import vis.vis.*;

public final class VisualGUIHex extends VisualGUI
{
	private static final double TEXT_END = 1.4;
	private static final double IMAGE_END = 1.0;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile lu;
	private final DoubleTile rle;
	private final DoubleTile rls;

	public VisualGUIHex(XGraphics graphics, HexCamera camera)
	{
		super(graphics);
		DoubleType y2 = camera.doubleType();
		lu = y2.createD(-1.0 / 6.0, 5.0 / 6.0, -4.0 / 6.0);
		rle = y2.createD(1.0 / 6.0, -5.0 / 6.0, 4.0 / 6.0);
		rls = y2.createD(4.0 / 6.0, -8.0 / 6.0, 4.0 / 6.0);
	}

	private DoubleTile rlLoc(DoubleType y2, GUIState xgui)
	{
		return y2.add(y2.fromTile(y2.fromOffset(xgui.xw() - 1, xgui.yw() - 1)), (xgui.yw() - 2) % 2 != 0 ? rls : rle);
	}

	@Override
	public boolean inside(TileCamera camera, DoubleTile h1, GUIState xgui)
	{
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		double xc = h1.v()[0] - h1.v()[1];
		double yc = h1.v()[2];
		DoubleTile rlLoc = rlLoc(camera.doubleType(), xgui);
		return xc >= lu.v()[0] - lu.v()[1] && xc <= rlLoc.v()[0] - rlLoc.v()[1] && yc >= lu.v()[2] && yc <= rlLoc.v()[2];
	}

	@Override
	public void locateAndDraw(TileCamera camera, GUIState xgui, Scheme scheme)
	{
		double cxs = xgui.xw() - (xgui.yw() > 1 ? 0.5 : 1.0) * HexMatrix.Q3 / 2.0;
		double cys = (xgui.yw() - 1) * 1.5 / 2.0;
		drawGUI(camera, xgui, scheme, cxs, cys, lu, rlLoc(camera.doubleType(), xgui), IMAGE_END, FONT_SIZE, TEXT_END);
	}
}