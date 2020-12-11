package editor.edit;

import geom.tile.*;
import logic.*;

public final class EditorSlot
{
	public final EditorSlotGUI gui;
	public EditingMode mode;

	public EditorSlot(EditingMode mode)
	{
		gui = new EditorSlotGUI();
		this.mode = mode;
	}

	public void setMode(EditingMode mode)
	{
		this.mode = mode;
	}

	public void onEnter(MainState mainState)
	{
		mode.onEnter(mainState);
	}

	public void onClick(MainState mainState, XKey key)
	{
		mode.onClick(mainState, key);
	}

	public void onMapClick(MainState mainState, Tile tile, XKey key)
	{
		mode.onMapClick(mainState, tile, key);
	}

	public void onMapDrag(MainState mainState, Tile tile1, Tile tile2, XKey key)
	{
		mode.onMapDrag(mainState, tile1, tile2, key);
	}
}