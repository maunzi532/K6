package logic.xstate;

import entity.*;
import logic.*;

public class EnemyStartState implements NAutoState
{
	private int counter;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
		for(XCharacter xEnemy : mainState.levelMap.teamCharacters(CharacterTeam.ENEMY))
		{
			xEnemy.newResources(new TurnResources(xEnemy.location(),
					mainState.combatSystem.movement(xEnemy),
					mainState.combatSystem.dashMovement(xEnemy), 2));
		}
	}

	@Override
	public String text()
	{
		return "Enemy Phase";
	}

	@Override
	public String keybind()
	{
		return "End Turn";
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