package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.input.*;
import levelMap.*;
import logic.*;

public class EditMoveState implements NMarkState, NEditState
{
	private XEntity entity;

	public EditMoveState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public String text()
	{
		return "Move";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.M;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, int key)
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