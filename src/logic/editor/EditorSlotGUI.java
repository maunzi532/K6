package logic.editor;

import file.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class EditorSlotGUI extends XGUIState
{
	public EditorSlotGUI()
	{
		initTiles();
	}

	public void setTile(GuiTile guiTile, boolean active, ColorScheme colorScheme)
	{
		if(active)
			tiles[0][0] = new GuiTile(guiTile.text, guiTile.imageName, guiTile.flipped, colorScheme.color("gui.editor.slot.active"));
		else
			tiles[0][0] = guiTile;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public int xw()
	{
		return 1;
	}

	@Override
	public int yw()
	{
		return 1;
	}

	@Override
	public void target(int x, int y){}

	@Override
	public void click(int x, int y, XKey key, XStateHolder stateHolder){}
}