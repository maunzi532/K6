package logic.xstate;

import logic.*;
import animation.*;
import system.*;

public final class PostAttackState extends AttackState
{
	private final ACResult4 result;
	private GetExpAnim getExpAnim;
	private boolean firstEnter;
	private MainState mainState1;
	private boolean levelup;
	private boolean levelupT;

	public PostAttackState(NState nextState, AttackCalc4 aI, ACResult4 result)
	{
		super(nextState, aI);
		this.result = result;
		firstEnter = true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState1 = mainState;
		if(firstEnter)
		{
			aI.aI.initiator().setHP(result.hp1());
			aI.aI.target().setHP(result.hp2());
			if(result.hp1() <= 0)
				mainState.levelMap().setDefeated(aI.aI.initiator());
			if(result.hp2() <= 0)
				mainState.levelMap().setDefeated(aI.aI.target());
			getExpAnim = new GetExpAnim(aI, result, mainState.levelMap());
			firstEnter = false;
			levelup = getExpAnim.isLevelup();
			levelupT = getExpAnim.isLevelupT();
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
		if(mainState1.levelMap().isLose())
			return new LoseState();
		if(mainState1.levelMap().isWin())
			return new WinState();
		return nextState;
	}
}