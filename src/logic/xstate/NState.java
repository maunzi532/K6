package logic.xstate;

import logic.*;

public interface NState
{
	default boolean editMode()
	{
		return false;
	}

	void onEnter(MainState mainState);

	default String text()
	{
		throw new RuntimeException();
	}

	default String keybind()
	{
		throw new RuntimeException();
	}

	default boolean keepInMenu(MainState mainState)
	{
		return true;
	}

	XMenu menu();
}