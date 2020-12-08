package logic.xstate;

import system.*;

public abstract class AttackState implements NAutoState
{
	protected final NState nextState;
	protected final AttackCalc aI;

	protected AttackState(NState nextState, AttackCalc aI)
	{
		this.nextState = nextState;
		this.aI = aI;
	}
}