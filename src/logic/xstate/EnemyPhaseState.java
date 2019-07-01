package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import logic.*;

public class EnemyPhaseState implements NAutoState
{
	private EnemyMove initiativeMove;

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.clearSideInfo();
		initiativeMove = mainState.levelMap.getEntitiesE().stream().filter(XEnemy::canAttack).map(e -> e.preferredMove(false, 0))
				.max(Comparator.comparingInt(EnemyMove::getInitiative)).filter(e -> e.getInitiative() >= 0).orElse(null);
		if(initiativeMove != null && initiativeMove.moveTo() != null && initiativeMove.moveTo().movingAlly != null)
		{
			EnemyMove initiativeMove2 = ((XEnemy) initiativeMove.moveTo().movingAlly).preferredMove(true, initiativeMove.getTileAdvantage());
			if(initiativeMove2.getInitiative() >= 0)
				initiativeMove = initiativeMove2;
		}
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
			Tile moveTo = initiativeMove.moveTo().tile;
			AttackInfo attackInfo = initiativeMove.attackInfo();
			if(moveTo != null)
				initiativeMove.getEntity().setMoved();
			if(attackInfo != null)
			{
				initiativeMove.getEntity().setAttacked();
				if(moveTo != null)
					return new MoveAnimState(new PreAttackState(this, attackInfo), initiativeMove.getEntity(), moveTo);
				else
					return new PreAttackState(this, attackInfo);
			}
			else
			{
				if(moveTo != null)
					return new MoveAnimState(this, initiativeMove.getEntity(), moveTo);
				else
					return new ProductionPhaseState();
			}
		}
		else
		{
			return new ProductionPhaseState();
		}
	}

	@Override
	public String text()
	{
		return "Enemy Phase";
	}
}