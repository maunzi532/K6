package logic.xstate;

import entity.*;
import entity.analysis.*;
import java.util.function.*;
import logic.*;
import logic.gui.guis.*;
import system2.*;
import system2.analysis.*;
import system2.animation.*;

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
			arrow = new GetExpAnim(aI, (RNGOutcome2) result, mainState.levelMap);
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