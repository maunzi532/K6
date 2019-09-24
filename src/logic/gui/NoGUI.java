package logic.gui;

import logic.*;
import logic.xstate.*;

public class NoGUI extends NGUIState
{
	public static final NGUIState NONE = new NoGUI();

	public NoGUI()
	{
		tiles = new GuiTile[0][0];
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
	}

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
	public void target(int x, int y)
	{
		throw new RuntimeException();
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		throw new RuntimeException();
	}

	@Override
	public void close(XStateHolder stateHolder, boolean setState){}
}