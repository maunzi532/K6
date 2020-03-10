package logic.xstate;

import logic.*;

public class NoneState implements NState
{
	public static final NoneState INSTANCE = new NoneState();

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}