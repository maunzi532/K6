package xstate;

import logic.*;
import guis.*;

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
		//mainState.stateHolder().updateLevel(mainState.worldControl().createLevel());
	}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		return new LoadLevelGUI();
	}
}