package editor.editingmodes;

import geom.tile.*;
import gui.*;
import levelmap.*;
import logic.*;
import editor.edit.*;

public final class FloorSetMode implements EditingMode
{
	private final GuiTile guiTile;
	private final FloorTileType tileType;

	public FloorSetMode(FloorTileType tileType)
	{
		this.tileType = tileType;
		guiTile = new GuiTile(tileType.text);
	}

	@Override
	public GuiTile guiTile()
	{
		return guiTile;
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, XKey key)
	{
		if(key.hasFunction("edit.floorset.clear"))
		{
			mainState.levelMap().clearTile(tile);
		}
		if(key.hasFunction("edit.floorset.set"))
		{
			mainState.levelMap().setFloorTile(tile, new FloorTile(0, tileType));
		}
	}

	@Override
	public void onMapDrag(MainState mainState, Tile tile1, Tile tile2, XKey key)
	{
		if(key.hasFunction("edit.floorset.clear"))
		{
			mainState.levelMap().y1().betweenArea(tile1, tile2).forEach(mainState.levelMap()::clearTile);
		}
		if(key.hasFunction("edit.floorset.set"))
		{
			mainState.levelMap().y1().betweenArea(tile1, tile2).forEach(tile -> mainState.levelMap().setFloorTile(tile, new FloorTile(0, tileType)));
		}
	}
}