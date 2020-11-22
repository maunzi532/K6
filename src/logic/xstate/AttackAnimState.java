package logic.xstate;

import entity.*;
import java.util.*;
import logic.*;
import system4.*;

public final class AttackAnimState implements NAutoState
{
	private NState nextState;
	private XCharacter initiator;
	private XCharacter target;
	private AttackAnim4 attackAnim;

	public AttackAnimState(NState nextState, XCharacter initiator, XCharacter target)
	{
		this.nextState = nextState;
		this.initiator = initiator;
		this.target = target;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		attackAnim = new AttackAnim4(List.of(), mainState.levelMap(), initiator, target);
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
		return nextState;//new PostAttackState(nextState, aI, attackAnim.outcome());
	}
}