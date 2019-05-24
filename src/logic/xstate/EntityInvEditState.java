package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import logic.*;

public class EntityInvEditState implements NGUIState
{
	private XEntity entity;
	private MainState mainState;

	public EntityInvEditState(XEntity entity, MainState mainState)
	{
		this.entity = entity;
		this.mainState = mainState;
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		if(entity instanceof InvEntity)
			return new EntityInvEditGUI((InvEntity) entity, mainState.combatSystem.allItems());
		else
			return NoGUI.NONE;
	}

	@Override
	public String text()
	{
		return "Edit Inv.";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity, mainState);
	}

	@Override
	public boolean editMode()
	{
		return true;
	}
}