package gui.guis;

import gui.*;
import logic.xstate.*;

public class EditingGUI extends XGUI
{
	public EditingGUI()
	{
		initTiles();
		setTile(new GuiTile("T"));
	}

	public void setTile(GuiTile guiTile)
	{
		tiles[0][0] = guiTile;
	}

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
	public boolean click(int x, int y, int key, XStateHolder stateHolder)
	{
		return false;
	}
}