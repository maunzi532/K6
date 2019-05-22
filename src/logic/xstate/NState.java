package logic.xstate;

import logic.*;

public interface NState
{
	String text();

	XMenu menu();

	default boolean keepInMenu(MainState mainState)
	{
		return true;
	}

	default void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.clearSideInfo();
	}

	default boolean editMode()
	{
		return false;
	}
}