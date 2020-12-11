package xstate;

import logic.*;

public class StateReverter implements NAutoState
{
	private final NState revert;
	private final NState onChanged;
	private final NState onMainAction;
	private boolean changed;
	private boolean mainAction;

	public StateReverter(NState revert, NState onChanged, NState onMainAction)
	{
		this.revert = revert;
		this.onChanged = onChanged;
		this.onMainAction = onMainAction;
	}

	public void setChanged()
	{
		changed = true;
	}

	public void setMainAction()
	{
		mainAction = true;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public void tick(MainState mainState){}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		if(mainAction)
			return onMainAction;
		else if(changed)
			return onChanged;
		else
			return revert;
	}
}