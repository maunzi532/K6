package logic.editor.editingModes;

import geom.f1.*;
import levelMap.*;
import logic.*;
import logic.editor.*;
import logic.editor.xgui.*;
import logic.gui.*;
import logic.gui.guis.*;

public class BCCreateMode implements EditingMode
{
	public static final BCCreateMode INSTANCE = new BCCreateMode();

	@Override
	public GuiTile guiTile()
	{
		return new GuiTile("Create Object");
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, XKey key)
	{
		AdvTile advTile = mainState.levelMap.advTile(tile);
		if(mainState.stateHolder.preferBuildings())
		{
			if(advTile.building() == null)
			{
				mainState.stateHolder.setState(new SelectBuildingGUI(new EditModeBuilder(tile)));
			}
			else if(advTile.entity() == null)
			{
				mainState.stateHolder.setState(new EntityCreateGUI(tile));
			}
		}
		else
		{
			if(advTile.entity() == null)
			{
				mainState.stateHolder.setState(new EntityCreateGUI(tile));
			}
			else if(advTile.building() == null)
			{
				mainState.stateHolder.setState(new SelectBuildingGUI(new EditModeBuilder(tile)));
			}
		}
	}
}