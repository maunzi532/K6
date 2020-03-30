package logic.xstate;

import entity.sideinfo.*;
import levelMap.*;
import logic.*;

public class NoneState implements NState
{
	public static final NoneState INSTANCE = new NoneState();

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.clearSideInfo();
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}