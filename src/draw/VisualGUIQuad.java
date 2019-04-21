package draw;

import geom.*;
import geom.d1.*;
import gui.*;
import javafx.scene.canvas.*;

public class VisualGUIQuad extends VisualGUI
{
	private static final double TEXT_END = 1.35;
	private static final double FONT_SIZE = 0.5;
	private final DoubleTile LU;
	private final DoubleTile RL;

	public VisualGUIQuad(GraphicsContext gd, double xHalfWidth, double yHalfWidth)
	{
		super(gd, new QuadCamera(xHalfWidth, yHalfWidth, yHalfWidth / 8, yHalfWidth / 8, 0,  0));
		LU = this.y2.createD(-3d / 4d, -3d / 4d);
		RL = this.y2.createD(-1d / 4d, -1d / 4d);
	}

	private DoubleTile rl(XGUI xgui)
	{
		return y2.add(y2.createD(xgui.xw(), xgui.yw()), RL);
	}

	@Override
	public boolean inside(DoubleTile h1, XGUI xgui)
	{
		if(xgui.xw() <= 0 || xgui.yw() <= 0)
			return false;
		DoubleTile rl = rl(xgui);
		return h1.v[0] >= LU.v[0] && h1.v[0] <= rl.v[0] && h1.v[1] >= LU.v[1] && h1.v[1] <= rl.v[1];
	}

	@Override
	public void draw(XGUI xgui)
	{
		double cxs = (xgui.xw() - 1) * QuadLayout.DQ2;
		double cys = (xgui.yw() - 1) * QuadLayout.DQ2;
		draw1(xgui, cxs, cys, LU, rl(xgui), QuadLayout.DQ2, FONT_SIZE, TEXT_END);
	}
}