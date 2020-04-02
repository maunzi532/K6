package logic.gui.guis;

import building.adv.*;
import logic.*;
import logic.xstate.*;

public final class ProductionInvGUI extends Inv2GUI
{
	private final XBuilding building;
	private final ProcessInv processInv;

	public ProductionInvGUI(XBuilding building, ProcessInv processInv)
	{
		super(building);
		this.building = building;
		this.processInv = processInv;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		super.onEnter(mainState);
	}

	@Override
	public CharSequence text()
	{
		return "menu.inv.building";
	}

	@Override
	public String keybind()
	{
		return "state.inv.building";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building, processInv);
	}
}