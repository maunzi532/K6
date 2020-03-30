package logic.xstate;

import logic.*;
import statsystem.*;
import statsystem.analysis.*;
import statsystem.animation.*;

public final class AttackAnimState extends AttackState
{
	private RNGDivider2 divider;
	private AttackAnim attackAnim;

	public AttackAnimState(NState nextState, AttackInfo aI)
	{
		super(nextState, aI);
		divider = aI.analysis.getStart();
	}

	@Override
	public void onEnter(MainState mainState)
	{
		attackAnim = new AttackAnim(divider, mainState.levelMap());
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