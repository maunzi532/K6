package logic.editor.xstate;

import entity.*;
import logic.*;
import logic.xstate.*;

public final class EditDeleteState implements NAutoState
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
	public CharSequence text()
	{
		return "menu.edit.delete";
	}

	@Override
	public String keybind()
	{
		return "state.edit.delete";
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