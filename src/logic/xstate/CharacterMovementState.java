package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.*;

public class CharacterMovementState implements NMarkState
{
	private XHero character;

	public CharacterMovementState(XHero character)
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
		return XMenu.characterMenu(character);
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl)
	{
		character.setMoved();
		levelMap.moveEntity(character, mapTile);
		stateControl.setState(NoneState.INSTANCE);
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return new Pathing(levelMap.y1, character, character.movement(), levelMap).start().getEndpoints()
				.stream().collect(Collectors.toMap(e -> e, e -> MarkType.TARGET));
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return character.isReady() && character.canMove();
	}
}