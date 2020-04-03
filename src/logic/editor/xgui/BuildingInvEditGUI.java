package logic.editor.xgui;

import building.adv.*;
import doubleinv.*;
import java.util.*;
import logic.xstate.*;

public final class BuildingInvEditGUI extends InvEditGUI
{
	private final XBuilding doubleInv;
	private final TradeDirection invToEdit;

	public BuildingInvEditGUI(XBuilding doubleInv, TradeDirection invToEdit)
	{
		super(doubleInv.inv(invToEdit), doubleInv.name(), List.of());
		this.doubleInv = doubleInv;
		this.invToEdit = invToEdit;
	}

	@Override
	public CharSequence text()
	{
		return invToEdit == TradeDirection.GIVE ? "menu.edit.inv.building.output" : "menu.edit.inv.building.input";
	}

	@Override
	public String keybind()
	{
		return invToEdit == TradeDirection.GIVE ? "state.edit.inv.building.output" : "state.edit.inv.building.input";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.buildingEditMenu(doubleInv);
	}
}