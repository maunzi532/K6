package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.*;

public class AttackTargetState implements NMarkState
{
	private XHero character;

	public AttackTargetState(XHero character)
	{
		this.character = character;
	}

	@Override
	public String text()
	{
		return "Attack";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMenu(character);
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return character.isReady() && character.getAp() >= 2 &&
				character.attackRanges(false).stream().map(e -> mainState.y2.range(character.location(), e, e))
				.flatMap(Collection::stream).anyMatch(e -> character.isEnemy(mainState.levelMap.getEntity(e)));
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl)
	{
		stateControl.setState(new AttackInfoState(character, levelMap.getEntity(mapTile)));
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return character.attackRanges(false).stream().map(e -> levelMap.y1.range(character.location(), e, e))
				.flatMap(Collection::stream).map(levelMap::getEntity).filter(e -> character.isEnemy(e)).collect(
				Collectors.toMap(XEntity::location, e -> MarkType.TARGET));
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.setSideInfo(character.standardSideInfo(), null);
	}
}