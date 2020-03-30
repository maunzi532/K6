package logic.editor.xgui;

import building.adv.*;
import java.util.*;
import logic.xstate.*;

public final class BuildingInvEditGUI extends InvEditGUI
{
	private final XBuilding doubleInv;
	private final boolean editOutput;

	public BuildingInvEditGUI(XBuilding doubleInv, boolean editOutput)
	{
		super(editOutput ? doubleInv.outputInv() : doubleInv.inputInv(), doubleInv.name(), List.of());
		this.doubleInv = doubleInv;
		this.editOutput = editOutput;
	}

	@Override
	public String text()
	{
		return editOutput ? "Edit Output" : "Edit Input";
	}

	@Override
	public String keybind()
	{
		return editOutput ? "Edit Output" : "Edit Input";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.buildingEditMenu(doubleInv);
	}
}