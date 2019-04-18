package logic.xstate;

import entity.*;
import logic.*;

public class StartTurnState implements NAutoState
{
	private int counter;

	@Override
	public void tick(MainState mainState)
	{
		if(counter == 0)
		{
			mainState.levelMap.getEntitiesH().forEach(XHero::startTurn);
		}
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= 10;
	}

	@Override
	public NState nextState()
	{
		return NoneState.INSTANCE;
	}

	@Override
	public String text()
	{
		return "Start Turn";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}