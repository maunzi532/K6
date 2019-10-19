package logic.editor.xgui;

import building.transport.*;
import java.util.*;
import levelMap.*;
import logic.xstate.*;

public class BuildingInvEditGUI extends InvEditGUI
{
	private DoubleInv doubleInv;
	private boolean editOutput;

	public BuildingInvEditGUI(DoubleInv doubleInv, boolean editOutput)
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
		return XMenu.buildingEditMenu((MBuilding) doubleInv);
	}
}