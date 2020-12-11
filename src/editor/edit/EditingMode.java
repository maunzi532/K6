package editor.edit;

import geom.tile.*;
import logic.*;
import gui.*;

public interface EditingMode
{
	GuiTile guiTile();

	default void onEnter(MainState mainState){}

	default void onClick(MainState mainState, XKey key){}

	void onMapClick(MainState mainState, Tile tile, XKey key);

	default void onMapDrag(MainState mainState, Tile tile1, Tile tile2, XKey key){}
}