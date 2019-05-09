package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.*;

public class EditCopyState implements NMarkState
{
	private XEntity entity;

	public EditCopyState(XEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl)
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
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
	}

	@Override
	public boolean editMode()
	{
		return true;
	}
}