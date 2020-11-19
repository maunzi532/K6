package logic.xstate;

import entity.*;
import geom.tile.*;
import logic.*;
import statsystem.*;

public final class EnemyPhaseState implements NAutoState
{
	private EnemyMove initiativeMove;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		/*initiativeMove = mainState.levelMap().teamTargetCharacters(CharacterTeam.ENEMY).stream().filter(e -> e.resources().ready())
				.map(e -> e.preferredMove(false, 0))
				.max(Comparator.comparingInt(EnemyMove::initiative)).filter(e -> e.initiative() >= 0).orElse(null);
		if(initiativeMove != null && initiativeMove.moveTo() != null && initiativeMove.moveTo().movingAlly() != null)
		{
			EnemyMove initiativeMove2 = (initiativeMove.moveTo().movingAlly()).preferredMove(true, initiativeMove.tileAdvantage());
			if(initiativeMove2.initiative() >= 0)
				initiativeMove = initiativeMove2;
		}*/
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
		if(initiativeMove != null)
		{
			Tile moveTo = initiativeMove.moveTo().tile();
			AttackInfo attackInfo = initiativeMove.attackInfo();
			if(moveTo != null)
				initiativeMove.entity().resources().move(initiativeMove.moveTo().cost());
			if(attackInfo != null)
			{
				initiativeMove.entity().resources().action(true);
				if(moveTo != null)
					return new MoveAnimState(new PreAttackState(this, attackInfo), initiativeMove.entity(), moveTo);
				else
					return new PreAttackState(this, attackInfo);
			}
			else
			{
				if(moveTo != null)
					return new MoveAnimState(this, initiativeMove.entity(), moveTo);
				else
					return new StartTurnState();
			}
		}
		else
		{
			return new StartTurnState();
		}
	}
}