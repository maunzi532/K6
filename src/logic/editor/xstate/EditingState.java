package logic.editor.xstate;

import logic.*;
import logic.xstate.*;

public class EditingState implements NState
{
	public static final EditingState INSTANCE = new EditingState();

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side.clearSideInfo();
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}