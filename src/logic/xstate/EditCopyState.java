package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.input.*;
import levelMap.*;
import logic.*;

public class EditCopyState implements NMarkState, NEditState
{
	private XEntity entity;

	public EditCopyState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void onEnter(MainState mainState){}

	@Override
	public String text()
	{
		return "Copy";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.C;
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
			mainState.levelMap.addEntity(entity.copy(mapTile));
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