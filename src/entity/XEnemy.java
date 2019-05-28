package entity;

import entity.analysis.*;
import geom.f1.*;
import item.*;
import item.inv.*;
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

	public XEnemy(Tile location, MainState mainState, Stats stats, Inv inv)
	{
		super(location, mainState, stats, inv);
	}

	@Override
	public XEntity copy(Tile copyLocation)
	{
		XEnemy copy = new XEnemy(copyLocation, mainState, stats, inv.copy());
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
		HashMap<AttackInfo, Double> analysis = new HashMap<>();
		for(AttackInfo info : attackInfo)
		{
			if(!analysis.containsKey(info))
				analysis.put(info, mainState.combatSystem.enemyAIScore(new RNGInfoAnalysis(mainState.combatSystem.supplyDivider(info)).create().outcomes()));
		}
		if(!attackInfo.isEmpty())
		{
			Collections.shuffle(attackInfo);
			AttackInfo maxScore = attackInfo.stream().max(Comparator.comparingDouble(analysis::get)).orElseThrow();
			return new EnemyMove(this, 0x100 + RANDOM.nextInt(0x100), maxScore.loc, maxScore);
		}
		else if(canMove)
		{
			Collections.shuffle(v);
			return new EnemyMove(this, RANDOM.nextInt(0x100), v.get(0), null);
		}
		else
		{
			return new EnemyMove(this, -1, null, null);
		}
	}

	@Override
	public int classSave()
	{
		return 2;
	}
}