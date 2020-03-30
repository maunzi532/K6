package logic.xstate;

import building.transport.*;
import logic.*;

public final class TransportPhaseState implements NAutoState
{
	public static final int WAIT = 30;

	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		mainState.levelMap().transportPhase();
	}

	@Override
	public void tick(MainState mainState)
	{
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= Transport.TRANSPORT_TIME + WAIT;
	}

	@Override
	public NState nextState()
	{
		return new StartTurnState();
	}
}