package editor.editingmodes;

import geom.tile.*;
import levelmap.*;
import logic.*;
import editor.edit.*;
import editor.xgui.*;
import gui.*;

public final class EditMode implements EditingMode
{
	public static final EditMode INSTANCE = new EditMode();

	@Override
	public GuiTile guiTile()
	{
		return new GuiTile("editmode.edit");
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, XKey key)
	{
		AdvTile advTile = mainState.levelMap().advTile(tile);
		if(advTile.entity() != null)
		{
			mainState.stateHolder().setState(new EntityEditGUI(advTile.entity()));
		}
	}
}