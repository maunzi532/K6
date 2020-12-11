package xstate;

import logic.*;

public interface NAutoState extends NState
{
	@Override
	default XMenu menu()
	{
		return XMenu.NOMENU;
	}

	void tick(MainState mainState);

	boolean finished();

	NState nextState();
}