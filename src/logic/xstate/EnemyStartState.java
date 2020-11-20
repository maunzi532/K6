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
		mainState.levelMap().allCharacters().stream().filter(e -> e.team() == CharacterTeam.ENEMY).forEach(xEnemy ->
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
		});
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