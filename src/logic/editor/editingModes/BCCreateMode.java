package logic.editor.editingModes;

import entity.*;
import geom.f1.*;
import logic.editor.xgui.*;
import logic.gui.*;
import levelMap.*;
import logic.editor.*;
import logic.*;

public class BCCreateMode implements EditingMode
{
	public static final BCCreateMode INSTANCE = new BCCreateMode();

	@Override
	public GuiTile guiTile()
	{
		return new GuiTile("Create Object");
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, int mouseKey)
	{
		AdvTile advTile = mainState.levelMap.advTile(tile);
		if(mainState.preferBuildings)
		{
			if(advTile.getBuilding() != null)
			{
				mainState.stateHolder.setState(new Inv1GUI_BES(advTile.getBuilding()));
			}
			else if(advTile.getEntity() != null)
			{
				if(advTile.getEntity() instanceof InvEntity)
				{
					mainState.stateHolder.setState(new EntityEditGUI((InvEntity) advTile.getEntity()));
				}
			}
		}
		else
		{
			if(advTile.getEntity() != null)
			{
				if(advTile.getEntity() instanceof InvEntity)
				{
					mainState.stateHolder.setState(new EntityEditGUI((InvEntity) advTile.getEntity()));
				}
			}
			else if(advTile.getBuilding() != null)
			{
				mainState.stateHolder.setState(new Inv1GUI_BES(advTile.getBuilding()));
			}
		}
	}
}