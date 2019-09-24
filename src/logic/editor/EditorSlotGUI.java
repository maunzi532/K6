package logic.editor;

import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class EditorSlotGUI extends XGUIState
{
	public EditorSlotGUI()
	{
		initTiles();
		setTile(new GuiTile("T"));
	}

	public void setTile(GuiTile guiTile)
	{
		tiles[0][0] = guiTile;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public int xw()
	{
		return 1;
	}

	@Override
	public int yw()
	{
		return 1;
	}

	@Override
	public void target(int x, int y){}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder){}
}