package logic.xstate;

import entity.*;
import logic.*;

public final class RevertMovementState implements NAutoState
{
	private final XCharacter character;

	public RevertMovementState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		//mainState.levelMap().revertMovement(character);
	}

	@Override
	public CharSequence text()
	{
		return "menu.revertmove";
	}

	@Override
	public String keybind()
	{
		return "state.revertmove";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return true;//character.resources().canRevertMoveAction();
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
		return new AdvMoveState(character);
	}
}