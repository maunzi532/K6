package logic.xstate;

import logic.*;

public final class EndTurnState implements NAutoState
{
	private boolean start;

	@Override
	public void onEnter(MainState mainState){}

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
	public CharSequence text()
	{
		return start ? "menu.endturn.start" : "menu.endturn.endturn";
	}

	@Override
	public String keybind()
	{
		return "state.endturn";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		start = mainState.levelMap().turnCounter() == 0;
		return true;
	}
}