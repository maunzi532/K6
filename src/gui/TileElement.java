package gui;

import java.util.function.*;

public class TileElement implements GuiElement
{
	private final AreaTile tile;
	public GuiTile fillTile;
	private boolean targetable;
	private Supplier<Boolean> exists;
	protected boolean targeted;
	private Runnable onClick;

	public TileElement(AreaTile tile)
	{
		this.tile = tile;
	}

	public TileElement(AreaTile tile, GuiTile fillTile)
	{
		this.tile = tile;
		this.fillTile = fillTile;
	}

	public TileElement(AreaTile tile, boolean targetable, Supplier<Boolean> exists, Runnable onClick)
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
		for(int ix = 0; ix < tile.right; ix++)
		{
			for(int iy = 0; iy < tile.down; iy++)
			{
				if(ix == tile.right - 1 && iy == tile.down - 1)
					tiles[tile.x + ix][tile.y + iy] = new GuiTile(guiTile, tile.right, tile.down);
				else
					tiles[tile.x + ix][tile.y + iy] = AreaTile.getOther(guiTile);
			}
		}
	}

	private void setFilledTile(GuiTile[][] tiles)
	{
		for(int ix = 0; ix < tile.right; ix++)
		{
			for(int iy = 0; iy < tile.down; iy++)
			{
				if(ix == tile.right - 1 && iy == tile.down - 1)
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
				targeted = true;
			}
			return new ElementTargetResult(true, requireUpdate, tile);
		}
		else
			return new ElementTargetResult(false, false, null);
	}
}