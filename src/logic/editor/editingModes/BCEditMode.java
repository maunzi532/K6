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
		AdvTile advTile = mainState.levelMap.advTile(tile);
		if(mainState.preferBuildings)
		{
			if(advTile.getBuilding() != null)
			{
				mainState.stateHolder.setState(new BuildingInvEditGUI(advTile.getBuilding(), false));
			}
			else if(advTile.getEntity() != null)
			{
				mainState.stateHolder.setState(new EntityEditGUI(advTile.getEntity()));
			}
		}
		else
		{
			if(advTile.getEntity() != null)
			{
				mainState.stateHolder.setState(new EntityEditGUI(advTile.getEntity()));
			}
			else if(advTile.getBuilding() != null)
			{
				mainState.stateHolder.setState(new BuildingInvEditGUI(advTile.getBuilding(), false));
			}
		}
	}
}