package logic.editor.xstate;

import entity.*;
import entity.sideinfo.*;
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
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState){}

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
	public void onClick(MainState mainState, LevelMap levelMap, XStateHolder stateHolder, Tile mapTile, XKey key)
	{
		if(levelMap.getEntity(mapTile) == null)
		{
			levelMap.addEntity(entity.copy(mapTile));
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