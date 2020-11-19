package logic.xstate;

import entity.*;
import logic.*;

public final class EnemyStartState implements NAutoState
{
	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		for(XCharacter xEnemy : mainState.levelMap().teamCharacters(CharacterTeam.ENEMY))
		{
			xEnemy.startTurn();
			if(xEnemy.targetable())
			{
				xEnemy.newResources(new TurnResources(xEnemy.location(), xEnemy.movement()));
			}
			else
			{
				xEnemy.newResources(new TurnResources(xEnemy.location()));
			}
		}
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