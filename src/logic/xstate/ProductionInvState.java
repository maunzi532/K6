package logic.xstate;

import building.*;
import gui.*;
import gui.guis.*;
import javafx.scene.input.*;
import logic.*;

public class ProductionInvState implements NGUIState
{
	private ProductionBuilding building;

	public ProductionInvState(ProductionBuilding building)
	{
		this.building = building;
	}

	@Override
	public String text()
	{
		return "Inv.";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.I;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building);
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new Inv2GUI(building);
	}
}