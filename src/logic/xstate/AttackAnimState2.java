package logic.xstate;

import entity.*;
import entity.analysis.*;
import java.util.function.*;
import logic.*;

public class AttackAnimState2 extends AttackState
{
	private RNGDivider divider;
	private AnimTimer arrow;
	private Supplier<RNGOutcome> outcomeSupplier;

	public AttackAnimState2(NState nextState, AttackInfo aI)
	{
		super(nextState, aI);
		divider = aI.analysis.getStart();
	}

	@Override
	public void onEnter(MainState mainState)
	{
		arrow = mainState.combatSystem.createAnimationTimer(divider, mainState);
		outcomeSupplier = (Supplier<RNGOutcome>) arrow;
	}

	@Override
	public void tick(MainState mainState)
	{
		arrow.tick();
	}

	@Override
	public boolean finished()
	{
		return arrow.finished();
	}

	@Override
	public NState nextState()
	{
		return new PostAttackState(nextState, aI, outcomeSupplier.get());
	}
}