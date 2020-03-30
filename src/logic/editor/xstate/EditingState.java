package logic.editor.xstate;

import entity.sideinfo.*;
import levelMap.*;
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
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.clearSideInfo();
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}
}