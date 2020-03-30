package logic.editor.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import levelMap.*;
import logic.*;
import logic.xstate.*;

public class EditCopyState implements NMarkState
{
	private final XCharacter entity;

	public EditCopyState(XCharacter entity)
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
		return "Copy";
	}

	@Override
	public String keybind()
	{
		return "Entity Copy";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
	}

	@Override
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		if(mainState.levelMap.getEntity(mapTile) == null)
		{
			mainState.levelMap.addEntity(entity.copy(mapTile));
		}
		else
		{
			mainState.stateHolder.setState(EditingState.INSTANCE);
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return List.of();
	}
}