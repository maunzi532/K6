package gui.guis;

import gui.*;
import logic.*;

public class EditingGUI extends XGUI
{
	public EditingGUI()
	{
		initTiles();
		tiles[0][0] = new GuiTile("T");
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
	public void target(int x, int y)
	{

	}

	@Override
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		System.out.println("W");
		return false;
	}
}