package logic.gui.guis;

import building.*;
import javafx.scene.input.*;
import logic.*;
import logic.xstate.*;

public class ProductionInvGUI extends Inv2GUI
{
	private ProductionBuilding building;

	public ProductionInvGUI(ProductionBuilding building)
	{
		super(building);
		this.building = building;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
		super.onEnter(mainState);
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
}