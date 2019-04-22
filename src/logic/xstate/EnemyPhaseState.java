package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import logic.*;

public class EnemyPhaseState implements NAutoState
{
	private boolean started;
	private EnemyMove initiativeMove;

	@Override
	public void onEnter(MainState mainState)
	{
		if(!started)
		{
			mainState.levelMap.getEntitiesE().forEach(XEnemy::startTurn);
			started = true;
		}
		initiativeMove = mainState.levelMap.getEntitiesE().stream().filter(XEnemy::canAttack).map(XEnemy::preferredMove)
				.max(Comparator.comparingInt(EnemyMove::getInitiative)).filter(e -> e.getInitiative() >= 0).orElse(null);
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
			Tile moveTo = initiativeMove.moveTo();
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

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}