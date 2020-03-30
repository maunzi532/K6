package logic.editor.editingModes;

import doubleinv.*;
import geom.f1.*;
import levelMap.*;
import logic.gui.*;
import logic.editor.*;
import logic.*;

public class FloorSetMode implements EditingMode
{
	private final GuiTile guiTile;
	private final FloorTileType tileType;

	public FloorSetMode(FloorTileType tileType)
	{
		this.tileType = tileType;
		guiTile = new GuiTile(tileType.name());
	}

	@Override
	public GuiTile guiTile()
	{
		return guiTile;
	}

	@Override
	public void onMapClick(MainState mainState, LevelMap levelMap, Tile tile, XKey key)
	{
		if(key.hasFunction("Clear Tile"))
		{
			levelMap.clearTile(tile);
		}
		if(key.hasFunction("Set Tile"))
		{
			levelMap.setFloorTile(tile, new FloorTile(0, tileType));
		}
	}

	@Override
	public void onMapDrag(MainState mainState, LevelMap levelMap, Tile tile1, Tile tile2, XKey key)
	{
		if(key.hasFunction("Clear Tile"))
		{
			levelMap.y1.betweenArea(tile1, tile2).forEach(levelMap::clearTile);
		}
		if(key.hasFunction("Set Tile"))
		{
			levelMap.y1.betweenArea(tile1, tile2).forEach(tile -> levelMap.setFloorTile(tile, new FloorTile(0, tileType)));
		}
	}
}