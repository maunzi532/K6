package logic.xstate;

import system.*;

public abstract class AttackState implements NAutoState
{
	protected final NState nextState;
	protected final AttackCalc4 aI;

	protected AttackState(NState nextState, AttackCalc4 aI)
	{
		this.nextState = nextState;
		this.aI = aI;
	}
}