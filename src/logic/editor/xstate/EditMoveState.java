package logic.editor.xstate;

import entity.*;
import geom.tile.*;
import java.util.*;
import levelmap.*;
import logic.*;
import logic.xstate.*;

public final class EditMoveState implements NMarkState
{
	private final XCharacter entity;

	public EditMoveState(XCharacter entity)
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
	public CharSequence text()
	{
		return "menu.edit.move";
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
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		if(mainState.levelMap().getEntity(mapTile) == null)
		{
			mainState.levelMap().moveEntity(entity, mapTile);
		}
		else
		{
			mainState.stateHolder().setState(EditingState.INSTANCE);
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return List.of();
	}
}