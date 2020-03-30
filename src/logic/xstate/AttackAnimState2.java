package logic.xstate;

import entity.sideinfo.*;
import levelMap.*;
import logic.*;
import system2.*;
import system2.analysis.*;
import system2.animation.*;

public class AttackAnimState2 extends AttackState
{
	private RNGDivider2 divider;
	private AttackAnim attackAnim;

	public AttackAnimState2(NState nextState, AttackInfo aI)
	{
		super(nextState, aI);
		divider = aI.analysis.getStart();
	}

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		attackAnim = new AttackAnim(divider, levelMap);
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