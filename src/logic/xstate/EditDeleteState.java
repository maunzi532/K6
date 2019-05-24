package logic.xstate;

import entity.*;
import logic.*;

public class EditDeleteState implements NAutoState
{
	private XEntity entity;
	private MainState mainState;

	public EditDeleteState(XEntity entity, MainState mainState)
	{
		this.entity = entity;
		this.mainState = mainState;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.clearSideInfo();
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
		return XMenu.entityEditMenu(entity, mainState);
	}

	@Override
	public boolean editMode()
	{
		return true;
	}
}