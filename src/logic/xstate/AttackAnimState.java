package logic.xstate;

import logic.*;
import statsystem.*;
import statsystem.animation.*;

public final class AttackAnimState extends AttackState
{
	private AttackAnim attackAnim;

	public AttackAnimState(NState nextState, AttackInfo aI)
	{
		super(nextState, aI);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		attackAnim = new AttackAnim(aI.analysis.getStart(), mainState.levelMap());
	}

	@Override
	public void tick(MainState mainState)
	{
		attackAnim.tick();
	}

	@Override
	public boolean finished()
	{
		return attackAnim.finished();
	}

	@Override
	public NState nextState()
	{
		return new PostAttackState(nextState, aI, attackAnim.outcome());
	}
}