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
}