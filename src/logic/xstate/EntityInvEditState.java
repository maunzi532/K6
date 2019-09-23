package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import javafx.scene.input.*;
import logic.*;

public class EntityInvEditState implements NGUIState, NEditState
{
	private XEntity entity;

	public EntityInvEditState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public String text()
	{
		return "Edit Inv.";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.I;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		if(entity instanceof InvEntity)
			return new EntityInvEditGUI((InvEntity) entity, mainState.combatSystem.allItems());
		else
			return NoGUI.NONE;
	}
}