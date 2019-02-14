package logic.gui;

import logic.*;

public abstract class XGUI
{
	protected GuiTile[][] tiles;

	protected void initTiles()
	{
		tiles = new GuiTile[xw()][yw()];
		for(int i = 0; i < xw(); i++)
		{
			for(int j = 0; j < yw(); j++)
			{
				tiles[i][j] = GuiTile.EMPTY;
			}
		}
	}

	public GuiTile[][] getTiles()
	{
		return tiles;
	}

	public abstract int xw();

	public abstract int yw();

	public abstract boolean click(int x, int y, int key, XStateControl stateControl);

	public boolean clickOutside(int key, XStateControl stateControl)
	{
		close(stateControl);
		return true;
	}

	public abstract void close(XStateControl stateControl);

	public static String except1(int num)
	{
		if(num == 1)
			return null;
		return String.valueOf(num);
	}
}