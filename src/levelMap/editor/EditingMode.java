package levelMap.editor;

import geom.f1.*;
import gui.*;
import logic.*;

public interface EditingMode
{
	GuiTile guiTile();

	default void onClick(MainState mainState, int mouseKey){}

	boolean onMapClick(MainState mainState, Tile tile, int mouseKey);
}