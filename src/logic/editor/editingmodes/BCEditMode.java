package logic.editor.editingmodes;

import doubleinv.*;
import geom.tile.*;
import levelmap.*;
import logic.*;
import logic.editor.*;
import logic.editor.xgui.*;
import logic.gui.*;

public final class BCEditMode implements EditingMode
{
	public static final BCEditMode INSTANCE = new BCEditMode();

	@Override
	public GuiTile guiTile()
	{
		return new GuiTile("editmode.edit");
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, XKey key)
	{
		AdvTile advTile = mainState.levelMap().advTile(tile);
		if(mainState.stateHolder().preferBuildings())
		{
			if(advTile.building() != null)
			{
				mainState.stateHolder().setState(new BuildingInvEditGUI(advTile.building(), TradeDirection.GIVE));
			}
			else if(advTile.entity() != null)
			{
				mainState.stateHolder().setState(new EntityEditGUI(advTile.entity()));
			}
		}
		else
		{
			if(advTile.entity() != null)
			{
				mainState.stateHolder().setState(new EntityEditGUI(advTile.entity()));
			}
			else if(advTile.building() != null)
			{
				mainState.stateHolder().setState(new BuildingInvEditGUI(advTile.building(), TradeDirection.GIVE));
			}
		}
	}
}