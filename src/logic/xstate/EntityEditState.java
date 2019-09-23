package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import javafx.scene.input.*;
import logic.*;

public class EntityEditState implements NGUIState, NEditState
{
	private XEntity entity;

	public EntityEditState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public String text()
	{
		return "Edit";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.E;
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
			return new EntityEditGUI((InvEntity) entity);
		else
			return NoGUI.NONE;
	}
}