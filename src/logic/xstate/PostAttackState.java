package logic.xstate;

import logic.*;
import statsystem.animation.*;
import system4.*;

public final class PostAttackState extends AttackState
{
	//private final RNGOutcome2 result;
	private GetExpAnim getExpAnim;
	private boolean firstEnter;
	private boolean levelup;
	private boolean levelupT;

	public PostAttackState(NState nextState, AttackCalc4 aI/*, RNGOutcome2 result*/)
	{
		super(nextState, aI);
		//this.result = result;
		//firstEnter = true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		if(firstEnter)
		{
			getExpAnim = null;//new GetExpAnim(aI, result, mainState.levelMap());
			firstEnter = false;
			levelup = false;//getExpAnim.isLevelup();
			levelupT = false;//getExpAnim.isLevelupT();
		}
		if(levelup)
		{
			levelup = false;
			//mainState.stateHolder().setState(new CharacterLevelupGUI(aI.entity, this));
		}
		else if(levelupT)
		{
			levelupT = false;
			//mainState.stateHolder().setState(new CharacterLevelupGUI(aI.entityT, this));
		}
		else
		{
			//getExpAnim.start();
		}
	}

	@Override
	public void tick(MainState mainState)
	{
		//getExpAnim.tick();
	}

	@Override
	public boolean finished()
	{
		return true;//getExpAnim.finished();
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}
}