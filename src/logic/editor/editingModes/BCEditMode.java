package logic.editor.editingModes;

import entity.*;
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