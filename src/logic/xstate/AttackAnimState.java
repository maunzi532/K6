package logic.xstate;

import arrow.*;
import entity.*;
import javafx.scene.paint.*;
import logic.*;

public class AttackAnimState extends AttackState
{
	private final boolean inverse;
	private final int num;
	private int counter;
	private int counter2;
	private int counter2T;
	private InfoArrow infoE;
	private InfoArrow infoET;
	private BlinkArrow blinkArrow;

	public AttackAnimState(NState nextState, AttackInfo aI)
	{
		this(nextState, aI, 0, false);
	}

	@Override
	protected boolean inverse()
	{
		return inverse;
	}

	public AttackAnimState(NState nextState, AttackInfo aI, int num, boolean inverse)
	{
		super(nextState, aI);
		this.num = num;
		this.inverse = inverse;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		infoE = new InfoArrow(entity().location(), entityT().location(),
				80, entity() instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK,
				stats().getStat(0), stats().getMaxStat(0));
		mainState.levelMap.addArrow(infoE);
		infoET = new InfoArrow(entityT().location(), entity().location(),
				80, entityT() instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK,
				statsT().getStat(0), statsT().getMaxStat(0));
		mainState.levelMap.addArrow(infoET);
		XArrow arrow = XArrow.factory(entity().location(), entityT().location(),
				60, false, entity().getImage(), false);
		mainState.levelMap.addArrow(arrow);
		entity().setReplacementArrow(arrow);
	}

	@Override
	public void tick(MainState mainState)
	{
		if(counter == 40)
		{
			blinkArrow = new BlinkArrow(entityT().location(),
					40, false, entityT().getImage(), 10);
			mainState.levelMap.addArrow(blinkArrow);
			entityT().setReplacementArrow(blinkArrow);
		}
		if(counter >= 40 && counter % 3 == 0)
		{
			if(counter2 >= 0 && counter2 < Math.abs(aI.getChange(true, inverse)))
			{
				stats().change(aI.getChange(true, inverse) > 0);
				infoE.setData(stats().getStat(0));
				counter2++;
				if(stats().removeEntity())
				{
					mainState.levelMap.removeEntity(entity());
					mainState.levelMap.addArrow(new BlinkArrow(entity().location(),
							40, false, entity().getImage(), 5));
					counter2 = -1;
				}
			}
			else
			{
				counter2 = -1;
			}
			if(counter2T >= 0 && counter2T < Math.abs(aI.getChange(false, !inverse)))
			{
				statsT().change(aI.getChange(false, !inverse) > 0);
				infoET.setData(statsT().getStat(0));
				counter2T++;
				if(statsT().removeEntity())
				{
					if(blinkArrow != null)
						blinkArrow.remove();
					mainState.levelMap.removeEntity(entityT());
					mainState.levelMap.addArrow(new BlinkArrow(entityT().location(),
							40, false, entityT().getImage(), 5));
					counter2T = -1;
				}
			}
			else
			{
				counter2T = -1;
			}
		}
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= 80 && counter2 < 0 && counter2T < 0;
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