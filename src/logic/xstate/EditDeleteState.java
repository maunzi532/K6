package logic.xstate;

import entity.*;
import logic.*;

public class EditDeleteState implements NAutoState
{
	private XEntity entity;

	public EditDeleteState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.levelMap.removeEntity(entity);
	}

	@Override
	public void tick(MainState mainState){}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		return EditingState.INSTANCE;
	}

	@Override
	public String text()
	{
		return "Delete";
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