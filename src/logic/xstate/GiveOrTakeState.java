package logic.xstate;

import doubleinv.*;
import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;
import logic.*;
import logic.gui.guis.*;

public final class GiveOrTakeState implements NMarkState
{
	private final TradeDirection tradeDirection;
	private final XCharacter character;
	private List<DoubleInv> possibleTargets;
	private List<VisMark> visMarked;

	public GiveOrTakeState(TradeDirection tradeDirection, XCharacter character)
	{
		this.tradeDirection = tradeDirection;
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		LevelMap levelMap = mainState.levelMap();
		List<Tile> range = levelMap.y1.range(character.location(), 0, character.stats().maxAccessRange());
		possibleTargets = new ArrayList<>();
		if(levelMap.playerTradeableStorage())
			possibleTargets.add(levelMap.storage());
		range.stream().map(levelMap::getEntity).filter(e -> e != null && e != character && e.active()
				&& levelMap.playerTradeable(e)).forEachOrdered(possibleTargets::add);
		visMarked = possibleTargets.stream().map(e -> new VisMark(getLocation(e),
				"mark.trade.target", VisMark.d2)).collect(Collectors.toList());
	}

	@Override
	public CharSequence text()
	{
		return tradeDirection.text;
	}

	@Override
	public String keybind()
	{
		return tradeDirection.keybind;
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return mainState.levelMap().playerTradeable(character) && character.resources().ready(1);
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterGUIMenu(character);
	}

	@Override
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		List<DoubleInv> list = possibleTargets.stream().filter(e -> mapTile.equals(getLocation(e))).collect(Collectors.toList());
		if(list.isEmpty())
		{
			mainState.stateHolder().setState(NoneState.INSTANCE);
		}
		else if(list.size() == 1)
		{
			startTradeState(mainState.stateHolder(), list.get(0));
		}
		else
		{
			list.stream().filter(inv1 -> inv1 instanceof XCharacter).findFirst()
					.ifPresent(inv1 -> startTradeState(mainState.stateHolder(), inv1));
		}
	}

	private Tile getLocation(DoubleInv possibleTarget)
	{
		if(possibleTarget instanceof Storage storage)
			return character.location();
		else
			return possibleTarget.location();
	}

	private void startTradeState(XStateHolder stateHolder, DoubleInv inv1)
	{
		stateHolder.setState(switch(tradeDirection)
				{
					case GIVE -> new DirectedTradeGUI(character, inv1, character.resources());
					case TAKE -> new DirectedTradeGUI(inv1, character, character.resources());
				});
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}