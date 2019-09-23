package logic.xstate;

import java.util.*;
import javafx.scene.input.*;
import levelMap.*;
import logic.*;

public interface NState
{
	default String text()
	{
		return "Error";
	}

	default KeyCode keybind()
	{
		return null;
	}

	XMenu menu();

	default boolean keepInMenu(MainState mainState)
	{
		return true;
	}

	default void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
	}

	default boolean editMode()
	{
		return false;
	}

	default List<VisMark> visMarked(MainState mainState)
	{
		return List.of();
	}
}