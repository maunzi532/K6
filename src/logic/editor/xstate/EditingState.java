package logic.editor.xstate;

import logic.*;
import logic.xstate.*;

public class EditingState implements NState, NEditState
{
	public static final EditingState INSTANCE = new EditingState();

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}