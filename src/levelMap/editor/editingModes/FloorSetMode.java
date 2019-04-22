package levelMap.editor.editingModes;

import geom.f1.*;
import gui.*;
import levelMap.*;
import levelMap.editor.*;

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
	public void onClick(int mouseKey, boolean active){}

	@Override
	public void onMapClick(Tile tile, LevelMap levelMap, int mouseKey)
	{
		levelMap.setFloorTile(tile, new FloorTile(0, tileType));
	}
}