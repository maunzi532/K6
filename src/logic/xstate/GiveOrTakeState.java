package logic.xstate;

import entity.*;
import geom.f1.*;
import item.inv.transport.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.*;

public class GiveOrTakeState implements NMarkState
{
	private boolean give;
	private XHero character;

	public GiveOrTakeState(boolean give, XHero character)
	{
		this.give = give;
		this.character = character;
	}

	@Override
	public String text()
	{
		return give ? "Give" : "Take";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMenu(character);
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateControl stateControl)
	{
		if(give)
			stateControl.setState(new DirectedTradeState(character, (DoubleInv) levelMap.getBuilding(mapTile), character));
		else
			stateControl.setState(new DirectedTradeState((DoubleInv) levelMap.getBuilding(mapTile), character, character));
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return levelMap.y1.range(character.location(), 0, character.maxAccessRange()).stream()
				.filter(e -> levelMap.getBuilding(e) instanceof DoubleInv).collect(Collectors.toMap(e -> e, e -> MarkType.TARGET));
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.setSideInfo(character.standardSideInfo(), null);
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return character.ready(1);
	}
}