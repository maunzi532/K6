package logic.xstate;

import entity.*;
import logic.*;

public class PreAttackState extends AttackState
{
	public PreAttackState(NState nextState, AttackInfo aI)
	{
		super(nextState, aI);
	}

	@Override
	protected boolean inverse()
	{
		return false;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public void tick(MainState mainState){}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		return new AttackAnimState(nextState, aI);
	}
}