package logic.gui.guis;

import building.adv.*;
import entity.sideinfo.*;
import levelMap.*;
import logic.*;
import logic.xstate.*;

public class ProductionInvGUI extends Inv2GUI
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
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.clearSideInfo();
		super.onEnter(side, levelMap, mainState);
	}

	@Override
	public String text()
	{
		return "Inv.";
	}

	@Override
	public String keybind()
	{
		return "Production Inv";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building, processInv);
	}
}