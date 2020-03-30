package logic.xstate;

import entity.sideinfo.*;
import levelMap.*;
import logic.*;

public class EndTurnState implements NAutoState
{
	private boolean start;

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState){}

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
		return start ? new StartTurnState() : new EnemyStartState();
	}

	@Override
	public String text()
	{
		return start ? "Start Level" : "Enemy Phase";
	}

	@Override
	public String keybind()
	{
		return "End Turn";
	}

	@Override
	public boolean keepInMenu(MainState mainState, LevelMap levelMap)
	{
		start = mainState.turnCounter == 0;
		return true;
	}
}