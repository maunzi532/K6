package logic.xstate;

import entity.*;
import logic.*;

public class RevertMovementState implements NAutoState
{
	private XHero character;

	public RevertMovementState(XHero character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		character.revertMovement();
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

	@Override
	public String text()
	{
		return "Back";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return character.canRevert();
	}
}