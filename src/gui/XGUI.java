package gui;

import javafx.scene.paint.Color;
import logic.*;

public abstract class XGUI
{
	private static final Color BACKGROUND = Color.color(0.4, 0.4, 0.5);

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

	public void setTile(CTile tile, GuiTile guiTile)
	{
		tiles[tile.x][tile.y] = guiTile;
	}

	public void setTile(CTile tile)
	{
		tiles[tile.x][tile.y] = tile.guiTile;
	}

	public GuiTile tile(CTile tile)
	{
		return tiles[tile.x][tile.y];
	}

	public abstract int xw();

	public abstract int yw();

	public Color background()
	{
		return BACKGROUND;
	}

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