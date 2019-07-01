package levelMap.editor.editingModes;

import geom.f1.*;
import gui.*;
import levelMap.*;
import levelMap.editor.*;
import logic.*;
import logic.xstate.*;

public class BCEditMode implements EditingMode
{
	public static final BCEditMode INSTANCE = new BCEditMode();

	@Override
	public GuiTile guiTile()
	{
		return new GuiTile("Edit Object");
	}

	@Override
	public boolean onMapClick(MainState mainState, Tile tile, int mouseKey)
	{
		AdvTile advTile = mainState.levelMap.advTile(tile);
		if(mouseKey == 1)
		{
			if(advTile.getEntity() != null)
			{
				mainState.stateHolder.setState(new EntityEditState(advTile.getEntity(), mainState));
				return true;
			}
			else if(advTile.getBuilding() != null)
			{
				mainState.stateHolder.setState(new BuildingEditState(advTile.getBuilding()));
				return true;
			}
		}
		else if(mouseKey == 3)
		{
			if(advTile.getBuilding() != null)
			{
				mainState.stateHolder.setState(new BuildingEditState(advTile.getBuilding()));
				return true;
			}
			else if(advTile.getEntity() != null)
			{
				mainState.stateHolder.setState(new EntityEditState(advTile.getEntity(), mainState));
				return true;
			}
		}
		return false;
	}
}