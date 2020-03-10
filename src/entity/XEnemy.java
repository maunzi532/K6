package entity;

import geom.f1.*;
import item.*;
import item.inv.*;

public class XEnemy extends XEntity
{
	private EnemyAI think;
	private boolean canMove;
	private boolean canAttack;

	public XEnemy(Tile location, CombatSystem combatSystem, Stats stats, EnemyAI think, int weightLimit, ItemList itemList)
	{
		super(location, combatSystem, stats, weightLimit, itemList);
		this.think = think;
	}

	public XEnemy(Tile location, CombatSystem combatSystem, Stats stats, EnemyAI think, Inv inv)
	{
		super(location, combatSystem, stats, inv);
		this.think = think;
	}

	@Override
	public XEntity copy(Tile copyLocation)
	{
		XEnemy copy = new XEnemy(copyLocation, combatSystem, stats, think.copy(), inv.copy());
		copy.stats.autoEquip(copy);
		return copy;
	}

	@Override
	public boolean isEnemy(XEntity other)
	{
		return other instanceof XHero;
	}

	public void startTurn()
	{
		canMove = true;
		canAttack = true;
	}

	public boolean canMove()
	{
		return canMove && canAttack;
	}

	public boolean canAttack()
	{
		return canAttack;
	}

	public void setMoved()
	{
		canMove = false;
	}

	public void setAttacked()
	{
		canMove = false;
		canAttack = false;
	}

	public EnemyMove preferredMove(boolean hasToMove, int moveAway)
	{
		if(!canAttack)
			return new EnemyMove(this, -1, null, null, 0);
		return think.preferredMove(this, canMove, hasToMove, moveAway);
	}

	@Override
	public int classSave()
	{
		return 2;
	}
}