package logic.xstate;

import logic.*;

public interface NAutoState extends NState
{
	void tick(MainState mainState);

	boolean finished();

	NState nextState();

	@Override
	default XMenu menu()
	{
		return XMenu.NOMENU;
	}
}