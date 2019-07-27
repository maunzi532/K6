package logic.xstate;

import building.*;
import gui.*;
import gui.guis.*;
import javafx.scene.input.*;
import logic.*;

public class ProductionViewState implements NGUIState
{
	private ProductionBuilding building;

	public ProductionViewState(ProductionBuilding building)
	{
		this.building = building;
	}

	@Override
	public String text()
	{
		return "View";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.V;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building);
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new RecipeGUI(building);
	}
}