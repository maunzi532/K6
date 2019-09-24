package logic.editor;

import geom.f1.*;
import logic.gui.*;
import logic.*;

public interface EditingMode
{
	GuiTile guiTile();

	default void onEnter(MainState mainState){}

	default void onClick(MainState mainState, int mouseKey){}

	void onMapClick(MainState mainState, Tile tile, int mouseKey);

	default void onMapDrag(MainState mainState, Tile tile1, Tile tile2, int mouseKey){}
}