package editor.editingmodes;

import geom.tile.*;
import levelmap.*;
import logic.*;
import editor.edit.*;
import editor.xgui.*;
import gui.*;

public final class CreateMode implements EditingMode
{
	public static final CreateMode INSTANCE = new CreateMode();

	@Override
	public GuiTile guiTile()
	{
		return new GuiTile("editmode.create");
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, XKey key)
	{
		AdvTile advTile = mainState.levelMap().advTile(tile);
		if(advTile.entity() == null)
		{
			mainState.stateHolder().setState(new EntityCreateGUI(tile));
		}
	}
}