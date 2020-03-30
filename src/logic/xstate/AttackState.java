package logic.xstate;

import statsystem.*;

public abstract class AttackState implements NAutoState
{
	protected final NState nextState;
	protected final AttackInfo aI;

	protected AttackState(NState nextState, AttackInfo aI)
	{
		this.nextState = nextState;
		this.aI = aI;
	}
}