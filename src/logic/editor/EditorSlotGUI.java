package logic.editor;

import logic.*;
import gui.*;
import logic.xstate.*;

public final class EditorSlotGUI extends XGUIState
{
	public EditorSlotGUI()
	{
		initTiles();
	}

	public void setTile(GuiTile guiTile, boolean active)
	{
		if(active)
			tiles[0][0] = new GuiTile(guiTile.text, guiTile.imageName, guiTile.flipped, "gui.editor.slot.active");
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