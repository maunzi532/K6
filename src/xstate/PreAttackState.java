package xstate;

import logic.*;
import system.*;

public final class PreAttackState extends AttackState
{
	public PreAttackState(NState nextState, AttackCalc aI)
	{
		super(nextState, aI);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		//mainState.side().setAttackSideInfo(aI);
		//aI.stats.equipMode(aI.mode);
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
		return new AttackAnimState(nextState, aI);
	}
}