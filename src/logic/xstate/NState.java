package logic.xstate;

import entity.sideinfo.*;
import levelMap.*;
import logic.*;

public interface NState
{
	default boolean editMode()
	{
		return false;
	}

	void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState);

	default String text()
	{
		return null;
	}

	default String keybind()
	{
		throw new RuntimeException();
	}

	default boolean keepInMenu(MainState mainState, LevelMap levelMap)
	{
		return true;
	}

	XMenu menu();
}