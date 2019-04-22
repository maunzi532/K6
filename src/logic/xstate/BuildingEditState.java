package logic.xstate;

import building.*;
import gui.*;
import gui.guis.*;
import levelMap.*;
import logic.*;

public class BuildingEditState implements NGUIState
{
	private MBuilding building;

	public BuildingEditState(MBuilding building)
	{
		this.building = building;
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new Inv1GUI(((ProductionBuilding) building).getOutputInv());
	}

	@Override
	public String text()
	{
		return "Error";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}
}