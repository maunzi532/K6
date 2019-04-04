package logic.xstate;

import entity.hero.*;
import geom.f1.*;
import java.util.*;
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
		return true; //TODO
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl)
	{
		stateControl.setState(new AttackInfoState(character, levelMap.getEntity(mapTile)));
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return null; //TODO
	}
}