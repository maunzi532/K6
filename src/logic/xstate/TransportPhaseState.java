package logic.xstate;

import logic.*;

public class TransportPhaseState implements NAutoState
{
	public static final int TRANSPORT_TIME = 60;
	public static final int WAIT = 30;

	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.clearSideInfo();
		mainState.levelMap.transportPhase();
	}

	@Override
	public void tick(MainState mainState)
	{
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= TRANSPORT_TIME + WAIT;
	}

	@Override
	public NState nextState()
	{
		return new StartTurnState();
	}
}