package logic.editor;

import geom.f1.*;
import logic.*;

public class EditorSlot
{
	public final EditorSlotGUI gui;
	public EditingMode mode;

	public EditorSlot(EditingMode mode)
	{
		gui = new EditorSlotGUI();
		setMode(mode);
	}

	public void setMode(EditingMode mode)
	{
		this.mode = mode;
	}

	public void onEnter(MainState mainState)
	{
		mode.onEnter(mainState);
	}

	public void onClick(MainState mainState, int mouseKey)
	{
		mode.onClick(mainState, mouseKey);
	}

	public void onMapClick(MainState mainState, Tile tile, int mouseKey)
	{
		mode.onMapClick(mainState, tile, mouseKey);
	}

	public void onMapDrag(MainState mainState, Tile tile1, Tile tile2, int mouseKey)
	{
		mode.onMapDrag(mainState, tile1, tile2, mouseKey);
	}
}