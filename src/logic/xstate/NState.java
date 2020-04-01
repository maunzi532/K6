package logic.xstate;

import logic.*;

public interface NState
{
	default boolean editMode()
	{
		return false;
	}

	void onEnter(MainState mainState);

	default CharSequence text()
	{
		return null;
	}

	default String keybind()
	{
		throw new RuntimeException("Keybind not defined");
	}

	default boolean keepInMenu(MainState mainState)
	{
		return true;
	}

	XMenu menu();
}