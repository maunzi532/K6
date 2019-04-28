package logic.xstate;

import arrow.*;
import entity.*;
import logic.*;

public class AttackAnimState extends AttackState
{
	private final boolean inverse;
	private final int num;
	private EntityArrowC acE;
	private EntityArrowC acT;

	public AttackAnimState(NState nextState, AttackInfo aI)
	{
		this(nextState, aI, 0, false);
	}

	public AttackAnimState(NState nextState, AttackInfo aI, int num, boolean inverse)
	{
		super(nextState, aI);
		this.num = num;
		this.inverse = inverse;
	}

	@Override
	protected boolean inverse()
	{
		return inverse;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		int change = aI.getChange(true, inverse, num);
		int changeT = aI.getChange(false, !inverse, num);
		acE = new EntityArrowC(mainState, entity(), entityT().location(), 1, 0, 60,
				10, 3, stats().getStat(0), stats().getMaxStat(0), change, 40);
		acT = new EntityArrowC(mainState, entityT(), entity().location(), changeT < 0 ? 2 : 0, 40, 40,
				40, 3, statsT().getStat(0), statsT().getMaxStat(0), changeT, 40);
	}

	@Override
	public void tick(MainState mainState)
	{
		stats().change(acE.tick(mainState));
		if(stats().removeEntity())
			mainState.levelMap.removeEntity(entity());
		statsT().change(acT.tick(mainState));
		if(statsT().removeEntity())
			mainState.levelMap.removeEntity(entityT());
	}

	@Override
	public boolean finished()
	{
		return Math.min(acE.finished(), acT.finished()) >= 0;
	}

	@Override
	public NState nextState()
	{
		if(aI.getStats(inverse).removeEntity() || aI.getStats(!inverse).removeEntity())
			return new PostAttackState(nextState, aI);
		int nextNum = inverse ? num + 1 : num;
		if(nextNum < aI.attackCount(!inverse))
		{
			return new AttackAnimState(nextState, aI, nextNum, !inverse);
		}
		if(num + 1 < aI.attackCount(inverse))
		{
			return new AttackAnimState(nextState, aI, num + 1, inverse);
		}
		return new PostAttackState(nextState, aI);
	}
}