package levelMap.editor;

import geom.f1.*;
import gui.*;
import levelMap.*;

public interface EditingMode
{
	GuiTile guiTile();

	void onClick(int mouseKey);

	void onMapClick(Tile tile, LevelMap levelMap, int mouseKey);
}