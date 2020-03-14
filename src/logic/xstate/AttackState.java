package logic.xstate;

import system2.*;

public abstract class AttackState implements NAutoState
{
	protected final NState nextState;
	protected final AttackInfo aI;

	public AttackState(NState nextState, AttackInfo aI)
	{
		this.nextState = nextState;
		this.aI = aI;
	}
}