package logic.xstate;

import gui.*;
import logic.*;

public interface NGUIState extends NState
{
	@Override
	default void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
	}

	XGUI gui(MainState mainState);
}