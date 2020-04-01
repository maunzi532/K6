package logic.xstate;

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
		for(XCharacter xHero : mainState.levelMap().teamCharacters(CharacterTeam.HERO))
		{
			xHero.startTurn();
			if(xHero.targetable())
			{
				xHero.newResources(new TurnResources(xHero.location(), xHero.stats().movement(), xHero.stats().dashMovement(), 2));
			}
			else
			{
				xHero.newResources(new TurnResources(xHero.location()));
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
		return NoneState.INSTANCE;
	}
}