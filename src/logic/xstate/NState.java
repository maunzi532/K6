package logic.xstate;

import javafx.scene.input.*;
import logic.*;

public interface NState
{
	void onEnter(MainState mainState);

	default String text()
	{
		throw new RuntimeException();
	}

	default KeyCode keybind()
	{
		throw new RuntimeException();
	}

	default boolean keepInMenu(MainState mainState)
	{
		return true;
	}

	XMenu menu();
}