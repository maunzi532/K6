package entity;

import geom.f1.*;
import item.*;
import java.util.*;
import logic.*;

public class XEnemy extends InvEntity
{
	private static final Random RANDOM = new Random();

	private boolean canMove;
	private boolean canAttack;

	public XEnemy(Tile location, MainState mainState, Stats stats, int weightLimit, ItemList itemList)
	{
		super(location, mainState, stats, weightLimit, itemList);
	}

	@Override
	public String name()
	{
		return "XEnemy";
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
		return canMove;
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
		canAttack = false;
	}

	public EnemyMove preferredMove()
	{
		List<Tile> v = new ArrayList<>();
		List<AttackInfo> attackInfo = new ArrayList<>();
		if(canMove)
			v.addAll(new Pathing(mainState.y2, this, movement(), mainState.levelMap).start().getEndpoints());
		else
			v.add(location());
		for(Tile t : v)
		{
			attackInfo.addAll(mainState.combatSystem
					.attackInfo(mainState, this, t, stats, mainState.levelMap.getEntitiesH()));
		}
		if(!attackInfo.isEmpty())
		{
			Collections.shuffle(attackInfo);
			return new EnemyMove(this, 0x100 + RANDOM.nextInt(0x100), attackInfo.get(0).loc, attackInfo.get(0));
		}
		else
		{
			Collections.shuffle(v);
			return new EnemyMove(this, RANDOM.nextInt(0x100), v.get(0), null);
		}
	}
}