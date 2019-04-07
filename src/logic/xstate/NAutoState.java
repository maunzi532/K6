package logic.xstate;

import logic.*;

public interface NAutoState extends NState
{
	boolean tickCheckFinished(MainState mainState);

	NState nextState();
}