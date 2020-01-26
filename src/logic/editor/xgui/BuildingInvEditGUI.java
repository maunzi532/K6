package logic.editor.xgui;

import building.adv.*;
import building.transport.*;
import java.util.*;
import logic.xstate.*;

public class BuildingInvEditGUI extends InvEditGUI
{
	private XBuilding doubleInv;
	private boolean editOutput;

	public BuildingInvEditGUI(XBuilding doubleInv, boolean editOutput)
	{
		super(editOutput ? doubleInv.outputInv() : doubleInv.inputInv(), doubleInv.name(), List.of());
		this.doubleInv = doubleInv;
		this.editOutput = editOutput;
	}

	@Override
	public boolean editMode()
	{
		return true;
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