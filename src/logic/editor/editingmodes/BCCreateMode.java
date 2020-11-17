package logic.editor.editingmodes;

import geom.tile.*;
import levelmap.*;
import logic.*;
import logic.editor.*;
import logic.editor.xgui.*;
import logic.gui.*;

public final class BCCreateMode implements EditingMode
{
	public static final BCCreateMode INSTANCE = new BCCreateMode();

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