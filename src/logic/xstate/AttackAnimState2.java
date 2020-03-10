package logic.xstate;

import entity.*;
import entity.analysis.*;
import java.util.function.*;
import logic.*;

public class AttackAnimState2 extends AttackState
{
	private RNGDivider divider;
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
		animTimer = mainState.combatSystem.createAnimationTimer(divider);
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