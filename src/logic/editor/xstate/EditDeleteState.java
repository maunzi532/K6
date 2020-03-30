package logic.editor.xstate;

import entity.*;
import logic.*;
import logic.xstate.*;

public class EditDeleteState implements NAutoState
{
	private final XCharacter entity;

	public EditDeleteState(XCharacter entity)
	{
		this.entity = entity;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		mainState.levelMap().removeEntity(entity);
	}

	@Override
	public String text()
	{
		return "Delete";
	}

	@Override
	public String keybind()
	{
		return "Entity Delete";
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
}