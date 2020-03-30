package logic.xstate;

import entity.*;
import entity.sideinfo.*;
import levelMap.*;
import logic.*;

public class RevertMovementState implements NAutoState
{
	private final XCharacter character;

	public RevertMovementState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.setStandardSideInfo(character);
		levelMap.revertMovement(character);
	}

	@Override
	public String text()
	{
		return "Back";
	}

	@Override
	public String keybind()
	{
		return "Revert";
	}

	@Override
	public boolean keepInMenu(MainState mainState, LevelMap levelMap)
	{
		return character.resources().revertMoveAction();
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