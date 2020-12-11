package xstate;

import entity.*;
import logic.*;

public final class EnemyPhaseState implements NAutoState
{
	private EnemyMove initiativeMove;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		initiativeMove = mainState.levelMap().allCharacters().stream()
				.filter(e -> e.team() == CharacterTeam.ENEMY && e.hasMainAction())
				.map(e -> e.systemChar().enemyAI().preferredMove(e, mainState.levelMap()))
				.sorted().findFirst().orElse(null);
	}

	@Override
	public void tick(MainState mainState){}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		if(initiativeMove == null)
		{
			return new StartTurnState();
		}
		else
		{
			initiativeMove.character().setHasMainAction(false);
			if(initiativeMove.aI() == null)
			{
				return new MoveAnimState(this, initiativeMove.character(), initiativeMove.moveTo().tile());
			}
			else if(initiativeMove.attackFirst())
			{
				return new PreAttackState(new MoveAnimState(this, initiativeMove.character(), initiativeMove.moveTo().tile()), initiativeMove.aI());
			}
			else
			{
				return new MoveAnimState(new PreAttackState(this, initiativeMove.aI()), initiativeMove.character(), initiativeMove.moveTo().tile());
			}
		}
	}
}