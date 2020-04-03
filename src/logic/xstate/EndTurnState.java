package logic.xstate;

import logic.*;

public final class EndTurnState implements NAutoState
{
	private boolean started;

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
		return started ? new EnemyStartState() : new StartTurnState();
	}

	@Override
	public CharSequence text()
	{
		return started ? "menu.endturn.endturn" : "menu.endturn.start";
	}

	@Override
	public String keybind()
	{
		return "state.endturn";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		started = mainState.levelMap().levelStarted();
		return true;
	}
}