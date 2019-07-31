package logic.xstate;

import entity.*;
import logic.*;

public class PostAttackState extends AttackState
{
	public PostAttackState(NState nextState, AttackInfo aI)
	{
		super(nextState, aI);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.combatSystem.postAttack(aI);
	}

	@Override
	public void tick(MainState mainState)
	{
		if(finished())
		{
			mainState.visualSideInfo.clearSideInfo();
		}
	}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}
}