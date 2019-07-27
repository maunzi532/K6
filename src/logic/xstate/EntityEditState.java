package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import javafx.scene.input.*;
import logic.*;

public class EntityEditState implements NGUIState
{
	private XEntity entity;
	private MainState mainState;

	public EntityEditState(XEntity entity, MainState mainState)
	{
		this.entity = entity;
		this.mainState = mainState;
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
	public KeyCode keybind()
	{
		return KeyCode.E;
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