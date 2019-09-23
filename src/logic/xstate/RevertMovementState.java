package logic.xstate;

import entity.*;
import javafx.scene.input.*;
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
		mainState.sideInfoFrame.setSideInfo(character.standardSideInfo(), null);
		character.revertMovement();
	}

	@Override
	public String text()
	{
		return "Back";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.BACK_SPACE;
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return character.canRevert();
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