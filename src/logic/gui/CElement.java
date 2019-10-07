package logic.gui;

import java.util.function.*;

public class CElement implements GuiElement
{
	private CTile tile;
	public GuiTile fillTile;
	private boolean targetable;
	private Supplier<Boolean> exists;
	protected Supplier<Boolean> onTarget;
	private Runnable onClick;

	public CElement(CTile tile)
	{
		this.tile = tile;
	}

	public CElement(CTile tile, GuiTile fillTile)
	{
		this.tile = tile;
		this.fillTile = fillTile;
	}

	public CElement(CTile tile, boolean targetable, Supplier<Boolean> exists, Runnable onClick)
	{
		this.tile = tile;
		this.targetable = targetable;
		this.exists = exists;
		this.onClick = onClick;
	}

	@Override
	public void update(){}

	@Override
	public void draw(GuiTile[][] tiles)
	{
		if(exists == null || exists.get())
		{
			if(fillTile != null)
				setEmptyTileAndFill(tiles, fillTile);
			else
				setFilledTile(tiles);
		}
	}

	private void setEmptyTileAndFill(GuiTile[][] tiles, GuiTile guiTile)
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

	private void setFilledTile(GuiTile[][] tiles)
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

	@Override
	public ElementTargetResult target(int x, int y, boolean click)
	{
		if(targetable && (exists == null || exists.get()) && tile.contains(x, y))
		{
			boolean requireUpdate = false;
			if(click)
			{
				if(onClick != null)
				{
					onClick.run();
					requireUpdate = true;
				}
			}
			else
			{
				if(onTarget != null)
				{
					if(onTarget.get())
						requireUpdate = true;
				}
			}
			return new ElementTargetResult(true, requireUpdate, tile);
		}
		else
			return new ElementTargetResult(false, false, null);
	}
}