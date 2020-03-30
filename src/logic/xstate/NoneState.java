package logic.xstate;

import logic.*;

public final class NoneState implements NState
{
	public static final NoneState INSTANCE = new NoneState();

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}