package logic.xstate;

import entity.*;
import javafx.scene.input.*;
import logic.*;

public class EnemyStartState implements NAutoState
{
	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
		for(XEnemy xEnemy : mainState.levelMap.getEntitiesE())
		{
			xEnemy.startTurn();
		}
	}

	@Override
	public String text()
	{
		return "Enemy Phase";
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
		return new EnemyPhaseState();
	}
}