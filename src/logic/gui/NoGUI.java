package logic.gui;

import logic.XStateControl;

public class NoGUI extends XGUI
{
	public static final XGUI NONE = new NoGUI();

	@Override
	public int xw()
	{
		return 0;
	}

	@Override
	public int yw()
	{
		return 0;
	}

	@Override
	public void click(int x, int y, int key, XStateControl stateControl)
	{
		throw new RuntimeException();
	}

	@Override
	public void close(XStateControl stateControl){}
}