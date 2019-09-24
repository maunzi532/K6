package logic.editor.xstate;

import entity.*;
import javafx.scene.input.*;
import logic.*;
import logic.xstate.*;

public class EditDeleteState implements NAutoState, NEditState
{
	private XEntity entity;

	public EditDeleteState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
		mainState.levelMap.removeEntity(entity);
	}

	@Override
	public String text()
	{
		return "Delete";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.D;
	}

	@Override
	public void tick(MainState mainState){}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		return EditingState.INSTANCE;
	}
}