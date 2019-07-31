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
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.attackInfo(aI);
		mainState.combatSystem.preAttack(aI);
	}

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
		return new AttackAnimState2(nextState, aI);
	}
}