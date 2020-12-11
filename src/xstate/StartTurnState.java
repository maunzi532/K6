package xstate;

import entity.*;
import logic.*;

public final class StartTurnState implements NAutoState
{
	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		mainState.levelMap().increaseTurnCounter();
		mainState.levelMap().allCharacters().stream().filter(e -> e.team() == CharacterTeam.HERO).forEach(xHero ->
		{
			xHero.startTurn();
			if(xHero.targetable())
			{
				xHero.setHasMainAction(true);
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
		return NoneState.INSTANCE;
	}
}