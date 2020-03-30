package logic.editor.editingModes;

import geom.f1.*;
import levelMap.*;
import logic.*;
import logic.editor.*;
import logic.editor.xgui.*;
import logic.gui.*;

public class BCEditMode implements EditingMode
{
	public static final BCEditMode INSTANCE = new BCEditMode();

	@Override
	public GuiTile guiTile()
	{
		return new GuiTile("Edit Object");
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, XKey key)
	{
		AdvTile advTile = mainState.levelMap().advTile(tile);
		if(mainState.stateHolder().preferBuildings())
		{
			if(advTile.building() != null)
			{
				mainState.stateHolder().setState(new BuildingInvEditGUI(advTile.building(), false));
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
				mainState.stateHolder().setState(new BuildingInvEditGUI(advTile.building(), false));
			}
		}
	}
}