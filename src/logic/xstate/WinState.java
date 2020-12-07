package logic.xstate;

import logic.*;

public class WinState implements NAutoState
{
	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		System.out.println("Win");
	}

	@Override
	public void tick(MainState mainState)
	{

	}

	@Override
	public boolean finished()
	{
		return false;
	}

	@Override
	public NState nextState()
	{
		return NoneState.INSTANCE;
	}
}