package logic.xstate;

import java.util.*;
import logic.*;
import system.*;

public final class AttackAnimState extends AttackState
{
	private ACResult4 result;
	private AttackAnim4 attackAnim;

	public AttackAnimState(NState nextState, AttackCalc4 aI)
	{
		super(nextState, aI);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		result = ACResult4.calc1(aI, mainState.levelMap(), new Random());
		mainState.side().setAttackSideInfo(aI, result.hpBar1(), result.hpBar2());
		attackAnim = new AttackAnim4(result, mainState.levelMap(), aI.aI.initiator(), aI.aI.target());
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
		return new PostAttackState(nextState, aI, result);
	}
}