package logic.xstate;

import arrow.*;
import entity.*;
import logic.*;

public class AttackAnimState implements NAutoState
{
	private final AttackInfo attackInfo;
	private int counter;

	public AttackAnimState(AttackInfo attackInfo)
	{
		this.attackInfo = attackInfo;
	}

	@Override
	public void tick(MainState mainState)
	{
		if(counter == 0)
		{
			XArrow arrow = XArrow.factory(attackInfo.entity.location(), attackInfo.entityT.location(),
					60, false, attackInfo.entity.getImage(), false);
			mainState.levelMap.addArrow(arrow);
			attackInfo.entity.setReplacementArrow(arrow);
		}
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= 60;
	}

	@Override
	public NState nextState()
	{
		return NoneState.INSTANCE;
	}

	@Override
	public String text()
	{
		return "Error";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}