package logic.xstate;

import arrow.*;
import entity.*;
import entity.analysis.*;
import logic.*;

public class PostAttackState extends AttackState
{
	private RNGOutcome result;
	private AnimTimer arrow;

	public PostAttackState(NState nextState, AttackInfo aI, RNGOutcome result)
	{
		super(nextState, aI);
		this.result = result;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		arrow = mainState.combatSystem.createPostAttackAnimation(aI, result, mainState);
	}

	@Override
	public void tick(MainState mainState)
	{
		arrow.tick();
	}

	@Override
	public boolean finished()
	{
		return arrow.finished();
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}
}