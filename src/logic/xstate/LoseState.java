package logic.xstate;

import logic.*;

public class LoseState implements NAutoState
{
	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
	}

	@Override
	public void tick(MainState mainState)
	{
		mainState.stateHolder().updateLevel(mainState.world().createLevel());
	}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		return NoneState.INSTANCE;
	}
}