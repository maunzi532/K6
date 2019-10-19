package logic.editor.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import levelMap.*;
import logic.*;
import logic.xstate.*;

public class EditMoveState implements NMarkState
{
	private XEntity entity;

	public EditMoveState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public String text()
	{
		return "Move";
	}

	@Override
	public String keybind()
	{
		return "Entity Move";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, XKey key)
	{
		if(mainState.levelMap.getEntity(mapTile) == null)
		{
			mainState.levelMap.moveEntity(entity, mapTile);
		}
		else
		{
			stateHolder.setState(EditingState.INSTANCE);
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return List.of();
	}
}