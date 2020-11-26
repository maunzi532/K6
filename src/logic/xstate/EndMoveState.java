package logic.xstate;

import entity.*;
import logic.*;

public class EndMoveState implements NAutoState
{
	private final XCharacter character;

	public EndMoveState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		character.setHasMainAction(false);
	}

	@Override
	public void tick(MainState mainState){}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public CharSequence text()
	{
		return "End";
	}

	@Override
	public String keybind()
	{
		return "state.endturn";
	}

	@Override
	public NState nextState()
	{
		return NoneState.INSTANCE;
	}
}