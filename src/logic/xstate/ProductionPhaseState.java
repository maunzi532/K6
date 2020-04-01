package logic.xstate;

import building.adv.*;
import logic.*;

public final class ProductionPhaseState implements NAutoState
{
	private static final int WAIT = 30;

	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		mainState.levelMap().productionPhase();
	}

	@Override
	public void tick(MainState mainState)
	{
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= ProcessInv.ARROW_TIME + WAIT;
	}

	@Override
	public NState nextState()
	{
		return new TransportPhaseState();
	}
}