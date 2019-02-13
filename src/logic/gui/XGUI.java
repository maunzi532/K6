package logic.gui;

import logic.*;

public abstract class XGUI
{
	protected GuiTile[][] tiles;

	public XGUI()
	{
		initTiles();
	}

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

	public abstract void click(int x, int y, int key, XStateControl stateControl);

	public void clickOutside(int key, XStateControl stateControl)
	{
		close(stateControl);
	}

	public abstract void close(XStateControl stateControl);
}