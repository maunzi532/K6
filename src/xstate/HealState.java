package xstate;

import entity.*;
import logic.*;
import system.*;

public class HealState implements NAutoState
{
	private final NState nextState;
	private final XCharacter healer;
	private final XCharacter target;
	private final int healAmount;
	private HealAnim healAnim;

	public HealState(NState nextState, XCharacter healer, XCharacter target, int healAmount)
	{
		this.nextState = nextState;
		this.healer = healer;
		this.target = target;
		this.healAmount = healAmount;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		healAnim = new HealAnim(target, mainState.levelMap(), healAmount);
	}

	@Override
	public void tick(MainState mainState)
	{
		healAnim.tick();
	}

	@Override
	public boolean finished()
	{
		return healAnim.finished();
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}
}