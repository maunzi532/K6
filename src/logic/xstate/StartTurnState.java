package logic.xstate;

import entity.*;
import javafx.scene.input.*;
import logic.*;

public class StartTurnState implements NAutoState
{
	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
		mainState.turnCounter++;
		for(XHero xHero : mainState.levelMap.getEntitiesH())
		{
			xHero.startTurn();
		}
	}

	@Override
	public String text()
	{
		return "Start";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.ENTER;
	}

	@Override
	public void tick(MainState mainState)
	{
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
}