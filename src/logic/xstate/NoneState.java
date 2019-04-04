package logic.xstate;

import logic.*;

public class NoneState implements NClickState
{
	public static final NoneState INSTANCE = new NoneState();

	@Override
	public String text()
	{
		return "Error";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	@Override
	public void onMenuClick(int key, MainState mainState){}
}