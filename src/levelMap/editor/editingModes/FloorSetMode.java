package levelMap.editor.editingModes;

import geom.f1.*;
import gui.*;
import levelMap.*;
import levelMap.editor.*;
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
	public void onMapClick(MainState mainState, Tile tile, int mouseKey)
	{
		if(mouseKey == 1)
		{
			mainState.levelMap.clearTile(tile);
		}
		if(mouseKey == 3)
		{
			mainState.levelMap.setFloorTile(tile, new FloorTile(0, tileType));
		}
	}

	@Override
	public void onMapDrag(MainState mainState, Tile tile1, Tile tile2, int mouseKey)
	{
		if(mouseKey == 1)
		{
			mainState.y2.betweenArea(tile1, tile2).forEach(mainState.levelMap::clearTile);
		}
		if(mouseKey == 3)
		{
			mainState.y2.betweenArea(tile1, tile2)
					.forEach(tile -> mainState.levelMap.setFloorTile(tile, new FloorTile(0, tileType)));
		}
	}
}