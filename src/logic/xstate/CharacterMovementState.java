package logic.xstate;

import entity.*;
import entity.hero.*;
import geom.f1.*;
import java.util.*;
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
		levelMap.moveEntity(character, mapTile);
		stateControl.setNState(NoneState.INSTANCE);
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return new Pathing(levelMap.y1, character, 4/*TODO*/, levelMap).start().getEndpoints();
	}
}