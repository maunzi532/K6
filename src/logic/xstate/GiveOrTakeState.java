package logic.xstate;

import entity.hero.*;
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
			stateControl.setNState(new DirectedTradeState(character, (DoubleInv) levelMap.getBuilding(mapTile)));
		else
			stateControl.setNState(new DirectedTradeState((DoubleInv) levelMap.getBuilding(mapTile), character));
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return levelMap.y1.range(character.location(), 0, 4/*TODO*/).stream()
				.filter(e -> levelMap.getBuilding(e) instanceof DoubleInv).collect(Collectors.toMap(e -> e, e -> MarkType.TARGET));
	}
}