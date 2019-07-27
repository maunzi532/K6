package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import levelMap.*;
import logic.*;

public class EditCopyState implements NMarkState
{
	private XEntity entity;
	private MainState mainState;

	public EditCopyState(XEntity entity, MainState mainState)
	{
		this.entity = entity;
		this.mainState = mainState;
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateHolder stateHolder)
	{
		levelMap.addEntity(entity.copy(mapTile));
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return levelMap.noEntityTiles().stream().collect(Collectors.toMap(e -> e, e -> MarkType.TARGET));
	}

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
		return XMenu.entityEditMenu(entity, mainState);
	}

	@Override
	public boolean editMode()
	{
		return true;
	}
}