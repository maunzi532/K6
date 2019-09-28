package logic.editor.xgui;

import building.transport.*;
import java.util.*;
import levelMap.*;
import logic.*;
import logic.gui.guis.*;

public class Inv1GUI_BES extends Inv1GUI
{
	public Inv1GUI_BES(MBuilding building)
	{
		super(((DoubleInv) building).outputInv(), ((DoubleInv) building).name(), List.of());
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		super.onEnter(mainState);
	}
}