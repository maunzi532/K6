package logic.xstate;

import entity.*;
import logic.*;

public class StartTurnState implements NAutoState
{
	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
		mainState.turnCounter++;
		for(XCharacter xHero : mainState.levelMap.teamCharacters(CharacterTeam.HERO))
		{
			xHero.newResources(new TurnResources(xHero.location(),
					xHero.stats().getMovement(), xHero.stats().dashMovement(), 2));
		}
	}

	@Override
	public String text()
	{
		return "Start";
	}

	@Override
	public String keybind()
	{
		return "Start Level";
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