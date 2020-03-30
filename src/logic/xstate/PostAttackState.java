package logic.xstate;

import logic.*;
import logic.gui.guis.*;
import statsystem.*;
import statsystem.analysis.*;
import statsystem.animation.*;

public final class PostAttackState extends AttackState
{
	private final RNGOutcome2 result;
	private GetExpAnim getExpAnim;
	private boolean firstEnter;
	private boolean levelup;
	private boolean levelupT;

	public PostAttackState(NState nextState, AttackInfo aI, RNGOutcome2 result)
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
			getExpAnim = new GetExpAnim(aI, result, mainState.levelMap());
			firstEnter = false;
			levelup = getExpAnim.isLevelup();
			levelupT = getExpAnim.isLevelupT();
		}
		if(levelup)
		{
			levelup = false;
			mainState.stateHolder().setState(new CharacterLevelupGUI(aI.entity, this));
		}
		else if(levelupT)
		{
			levelupT = false;
			mainState.stateHolder().setState(new CharacterLevelupGUI(aI.entityT, this));
		}
		else
		{
			getExpAnim.start();
		}
	}

	@Override
	public void tick(MainState mainState)
	{
		getExpAnim.tick();
	}

	@Override
	public boolean finished()
	{
		return getExpAnim.finished();
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}
}