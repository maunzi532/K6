package logic.editor;

import geom.f1.*;
import levelMap.*;
import logic.gui.*;
import logic.*;

public interface EditingMode
{
	GuiTile guiTile();

	default void onEnter(MainState mainState){}

	default void onClick(MainState mainState, XKey key){}

	void onMapClick(MainState mainState, LevelMap levelMap, Tile tile, XKey key);

	default void onMapDrag(MainState mainState, LevelMap levelMap, Tile tile1, Tile tile2, XKey key){}
}