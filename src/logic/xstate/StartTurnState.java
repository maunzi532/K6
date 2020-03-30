package logic.xstate;

import entity.*;
import entity.sideinfo.*;
import levelMap.*;
import logic.*;

public class StartTurnState implements NAutoState
{
	private int counter;

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.clearSideInfo();
		mainState.turnCounter++;
		for(XCharacter xHero : levelMap.teamCharacters(CharacterTeam.HERO))
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