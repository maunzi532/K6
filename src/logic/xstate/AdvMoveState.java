package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import levelMap.*;
import logic.*;

public class AdvMoveState implements NMarkState
{
	private XHero character;

	public AdvMoveState(XHero character)
	{
		this.character = character;
	}

	@Override
	public String text()
	{
		return "Move";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMoveMenu(character);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.setSideInfo(character.standardSideInfo(), null);
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		Map<Tile, MarkType> markMap = new HashMap<>();
		if(character.canMove())
		{
			new Pathing(levelMap.y1, character, character.movement(), levelMap, null).start().getEndpoints()
					.forEach(e -> markMap.put(e, MarkType.TARGET));
		}
		if(character.ready(2))
		{
			character.attackRanges(false).stream().map(e -> levelMap.y1.range(character.location(), e, e))
					.flatMap(Collection::stream).map(levelMap::getEntity).filter(e -> character.isEnemy(e))
					.forEach(e -> markMap.put(e.location(), MarkType.ON));;
		}
		return markMap;
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateHolder stateHolder)
	{
		if(markType == MarkType.TARGET)
		{
			character.setMoved();
			levelMap.moveEntity(character, mapTile);
			stateHolder.setState(new AdvMoveState(character));
		}
		else if(markType == MarkType.ON)
		{
			stateHolder.setState(new AttackInfoState(character, levelMap.getEntity(mapTile)));
		}
	}
}