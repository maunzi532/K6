package logic.xstate;

import arrow.*;
import entity.*;
import entity.analysis.*;
import logic.*;

public class AttackAnimState2 extends AttackState
{
	private RNGDivider divider;
	private AnimationArrow arrow;

	public AttackAnimState2(NState nextState, AttackInfo aI)
	{
		super(nextState, aI);
		divider = aI.analysis.getStart();
	}

	@Override
	protected boolean inverse()
	{
		return false;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public void tick(MainState mainState)
	{
		if(arrow == null || arrow.finished())
		{
			divider = divider.rollRNG();
			if(divider == null)
				return;
			arrow = mainState.combatSystem.createAnimationArrow(divider);
		}
		arrow.tick();
	}

	@Override
	public boolean finished()
	{
		return divider == null;
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}
}