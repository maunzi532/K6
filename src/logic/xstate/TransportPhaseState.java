package logic.xstate;

import logic.*;

public class TransportPhaseState implements NClickState
{
	public static final TransportPhaseState INSTANCE = new TransportPhaseState();

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
	public void onMenuClick(int key, MainState mainState)
	{
		mainState.levelMap.transportPhase();
	}
}