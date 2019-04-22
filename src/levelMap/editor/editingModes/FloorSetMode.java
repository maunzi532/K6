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
	public boolean onMapClick(MainState mainState, Tile tile, int mouseKey)
	{
		mainState.levelMap.setFloorTile(tile, new FloorTile(0, tileType));
		return false;
	}
}