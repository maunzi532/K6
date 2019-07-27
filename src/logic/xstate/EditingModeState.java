package logic.xstate;

import gui.*;
import gui.guis.*;
import levelMap.editor.*;
import logic.*;

public class EditingModeState implements NGUIState
{
	private LevelEditor editor;

	public EditingModeState(LevelEditor editor)
	{
		this.editor = editor;
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new EditingModeGUI(editor);
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}
}