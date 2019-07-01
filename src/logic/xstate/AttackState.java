package logic.xstate;

import entity.*;

public abstract class AttackState implements NAutoState
{
	protected final NState nextState;
	protected final AttackInfo aI;

	public AttackState(NState nextState, AttackInfo aI)
	{
		this.nextState = nextState;
		this.aI = aI;
	}

	protected abstract boolean inverse();

	protected XEntity entity()
	{
		return aI.getEntity(inverse());
	}

	protected XEntity entityT()
	{
		return aI.getEntity(!inverse());
	}

	protected Stats stats()
	{
		return aI.getStats(inverse());
	}

	protected Stats statsT()
	{
		return aI.getStats(!inverse());
	}

	@Override
	public String text()
	{
		return "Error";
	}
}