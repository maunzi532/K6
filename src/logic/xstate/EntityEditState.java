package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import logic.*;

public class EntityEditState implements NGUIState
{
	private XEntity entity;

	public EntityEditState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		if(entity instanceof InvEntity)
			return new EntityEditGUI((InvEntity) entity);
		else
			return NoGUI.NONE;
	}

	@Override
	public String text()
	{
		return "Edit";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
	}

	@Override
	public boolean editMode()
	{
		return true;
	}
}