package logic.xstate;

import building.adv.*;
import doubleinv.*;
import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.*;
import logic.gui.guis.*;

public class GiveOrTakeState implements NMarkState
{
	private final boolean give;
	private final XCharacter character;
	private List<DoubleInv> possibleTargets;
	private List<VisMark> visMarked;

	public GiveOrTakeState(boolean give, XCharacter character)
	{
		this.give = give;
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		boolean levelStarted = mainState.levelMap().turnCounter() > 0;
		List<Tile> range = mainState.levelMap().y1.range(character.location(), 0, character.stats().maxAccessRange());
		possibleTargets = new ArrayList<>();
		range.stream().map(mainState.levelMap()::getBuilding).filter(e -> e != null && e.active() && e.playerTradeable(levelStarted)).forEachOrdered(possibleTargets::add);
		range.stream().map(mainState.levelMap()::getEntity).filter(e -> e != null && e.active() && e.playerTradeable(levelStarted)).forEachOrdered(possibleTargets::add);
		visMarked = possibleTargets.stream().map(e -> new VisMark(e.location(), "mark.trade.target",
				e.type() == DoubleInvType.ENTITY ? VisMark.d2 : VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public String text()
	{
		return give ? "Give" : "Take";
	}

	@Override
	public String keybind()
	{
		return give ? "Give" : "Take";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		if(mainState.levelMap().turnCounter() == 0)
		{
			return character.saveSettings() == null || !character.saveSettings().startInvLocked;
		}
		else
		{
			return character.resources().ready(1);
		}
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterGUIMenu(character);
	}

	@Override
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		boolean levelStarted = mainState.levelMap().turnCounter() > 0;
		List<DoubleInv> list = possibleTargets.stream().filter(e -> mapTile.equals(e.location())).collect(Collectors.toList());
		if(list.isEmpty())
		{
			mainState.stateHolder().setState(NoneState.INSTANCE);
		}
		else if(list.size() == 1)
		{
			startTradeState(mainState.levelMap(), mainState.stateHolder(), list.get(0), levelStarted);
		}
		else
		{
			if(mainState.stateHolder().preferBuildings())
			{
				for(DoubleInv inv1 : list)
				{
					if(inv1 instanceof XBuilding)
					{
						startTradeState(mainState.levelMap(), mainState.stateHolder(), inv1, levelStarted);
						break;
					}
				}
			}
			else
			{
				for(DoubleInv inv1 : list)
				{
					if(inv1.type() == DoubleInvType.ENTITY)
					{
						startTradeState(mainState.levelMap(), mainState.stateHolder(), inv1, levelStarted);
						break;
					}
				}
			}
		}
	}

	private void startTradeState(LevelMap levelMap, XStateHolder stateHolder, DoubleInv inv1, boolean levelStarted)
	{
		if(inv1 == character)
		{
			if(!levelStarted)
			{
				if(give)
					stateHolder.setState(new DirectedTradeGUI(character, levelMap.storage(), null));
				else
					stateHolder.setState(new DirectedTradeGUI(levelMap.storage(), character, null));
			}
			else
			{
				stateHolder.setState(NoneState.INSTANCE);
			}
		}
		else
		{
			if(give)
				stateHolder.setState(new DirectedTradeGUI(character, inv1, levelStarted ? character.resources() : null));
			else
				stateHolder.setState(new DirectedTradeGUI(inv1, character, levelStarted ? character.resources() : null));
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}