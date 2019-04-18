package logic.xstate;

import logic.*;

public class TransportPhaseState implements NAutoState
{
	public static final int TRANSPORT_TIME = 60;
	public static final int WAIT = 30;

	private int counter;

	@Override
	public String text()
	{
		return "Transport";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	@Override
	public void tick(MainState mainState)
	{
		if(counter == 0)
		{
			mainState.levelMap.transportPhase();
		}
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