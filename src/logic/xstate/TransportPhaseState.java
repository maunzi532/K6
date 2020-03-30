package logic.xstate;

import building.transport.*;
import entity.sideinfo.*;
import levelMap.*;
import logic.*;

public class TransportPhaseState implements NAutoState
{
	public static final int WAIT = 30;

	private int counter;

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.clearSideInfo();
		levelMap.transportPhase();
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