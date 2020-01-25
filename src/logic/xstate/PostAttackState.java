package logic.xstate;

import arrow.*;
import entity.*;
import entity.analysis.*;
import java.util.function.*;
import logic.*;
import logic.gui.guis.*;

public class PostAttackState extends AttackState
{
	private RNGOutcome result;
	private AnimTimer arrow;
	private boolean firstEnter;
	private boolean levelup;
	private boolean levelupT;

	public PostAttackState(NState nextState, AttackInfo aI, RNGOutcome result)
	{
		super(nextState, aI);
		this.result = result;
		firstEnter = true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		if(firstEnter)
		{
			arrow = mainState.combatSystem.createPostAttackAnimation(aI, result, mainState);
			firstEnter = false;
			boolean[] v = ((Supplier<boolean[]>) arrow).get();
			levelup = v[0];
			levelupT = v[1];
		}
		if(levelup)
		{
			levelup = false;
			mainState.stateHolder.setState(new CharacterLevelupGUI(aI.entity, this));
		}
		else if(levelupT)
		{
			levelupT = false;
			mainState.stateHolder.setState(new CharacterLevelupGUI(aI.entityT, this));
		}
		else
		{
			((Runnable) arrow).run();
		}
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
		return nextState;
	}
}