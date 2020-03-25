package logic.xstate;

import entity.*;
import entity.analysis.*;
import java.util.function.*;
import logic.*;
import system2.*;
import system2.analysis.*;
import system2.animation.*;

public class AttackAnimState2 extends AttackState
{
	private RNGDivider2 divider;
	private AnimTimer animTimer;
	private Supplier<RNGOutcome> outcomeSupplier;

	public AttackAnimState2(NState nextState, AttackInfo aI)
	{
		super(nextState, aI);
		divider = aI.analysis.getStart();
	}

	@Override
	public void onEnter(MainState mainState)
	{
		animTimer = new AttackAnim(divider, mainState.levelMap, mainState.colorScheme);
		outcomeSupplier = (Supplier<RNGOutcome>) animTimer;
	}

	@Override
	public void tick(MainState mainState)
	{
		animTimer.tick();
	}

	@Override
	public boolean finished()
	{
		return animTimer.finished();
	}

	@Override
	public NState nextState()
	{
		return new PostAttackState(nextState, aI, outcomeSupplier.get());
	}
}