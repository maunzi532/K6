package gui;

import javafx.scene.paint.*;
import logic.xstate.*;

public abstract class XGUI
{
	private static final Color BACKGROUND = Color.color(0.4, 0.4, 0.5);

	protected GuiTile[][] tiles;
	private CTile targeted = CTile.NONE;

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

	public CTile getTargeted()
	{
		return targeted;
	}

	public void setTargeted(CTile targeted)
	{
		this.targeted = targeted;
	}

	public void setTile(CTile tile, GuiTile guiTile)
	{
		for(int ix = 0; ix < tile.r; ix++)
		{
			for(int iy = 0; iy < tile.d; iy++)
			{
				if(ix == tile.r - 1 && iy == tile.d - 1)
					tiles[tile.x + ix][tile.y + iy] = new GuiTile(guiTile, tile.r, tile.d);
				else
					tiles[tile.x + ix][tile.y + iy] = CTile.getOther(guiTile);
			}
		}
	}

	public void setTile(CTile tile)
	{
		for(int ix = 0; ix < tile.r; ix++)
		{
			for(int iy = 0; iy < tile.d; iy++)
			{
				if(ix == tile.r - 1 && iy == tile.d - 1)
					tiles[tile.x + ix][tile.y + iy] = tile.guiTile;
				else
					tiles[tile.x + ix][tile.y + iy] = tile.other;
			}
		}
	}

	public abstract int xw();

	public abstract int yw();

	public Color background()
	{
		return BACKGROUND;
	}

	public abstract void target(int x, int y);

	public abstract void click(int x, int y, int key, XStateHolder stateHolder);

	public void clickOutside(int key, XStateHolder stateHolder)
	{
		close(stateHolder, true);
	}

	public void close(XStateHolder stateHolder, boolean setState)
	{
		if(setState)
			stateHolder.setState(NoneState.INSTANCE);
	}
}