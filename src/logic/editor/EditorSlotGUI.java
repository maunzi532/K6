package logic.editor;

import javafx.scene.paint.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class EditorSlotGUI extends XGUIState
{
	public EditorSlotGUI()
	{
		initTiles();
	}

	public void setTile(GuiTile guiTile, boolean active)
	{
		if(active)
			tiles[0][0] = new GuiTile(guiTile.text, guiTile.image, guiTile.flipped, Color.LIGHTGRAY);
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
	public void click(int x, int y, int key, XStateHolder stateHolder){}
}