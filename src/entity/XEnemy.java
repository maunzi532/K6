package entity;

import geom.f1.*;
import item.*;
import item.inv.*;
import logic.*;

public class XEnemy extends XEntity
{
	private EnemyAI think;
	private boolean canMove;
	private boolean canAttack;

	public XEnemy(Tile location, MainState mainState, Stats stats, EnemyAI think, int weightLimit, ItemList itemList)
	{
		super(location, mainState, stats, weightLimit, itemList);
		this.think = think;
	}

	public XEnemy(Tile location, MainState mainState, Stats stats, EnemyAI think, Inv inv)
	{
		super(location, mainState, stats, inv);
		this.think = think;
	}

	@Override
	public XEntity copy(Tile copyLocation)
	{
		XEnemy copy = new XEnemy(copyLocation, mainState, stats, think.copy(), inv.copy());
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
		return think.preferredMove(mainState, this, canMove, hasToMove, moveAway);
	}

	@Override
	public int classSave()
	{
		return 2;
	}
}