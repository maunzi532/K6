package logic.xstate;

import building.transport.*;
import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;
import logic.gui.guis.*;

public class GiveOrTakeState implements NMarkState
{
	private boolean give;
	private XHero character;
	private List<DoubleInv> possibleTargets;
	private List<VisMark> visMarked;

	public GiveOrTakeState(boolean give, XHero character)
	{
		this.give = give;
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfoXH(character.standardSideInfo(), character);
		boolean levelStarted = mainState.turnCounter > 0;
		List<Tile> range = mainState.y1.range(character.location(), 0, character.maxAccessRange());
		possibleTargets = Stream.concat(range.stream().map(mainState.levelMap::getBuilding)
				.filter(DoubleInv::isTargetable).map(e -> (DoubleInv) e),
				range.stream().map(mainState.levelMap::getEntity)
						.filter(target -> DoubleInv.isTargetable(target) && target != character).map(e -> (DoubleInv) e))
				.filter(e -> e.playerTradeable(levelStarted)).collect(Collectors.toList());
		visMarked = possibleTargets.stream().map(e -> new VisMark(e.location(), Color.YELLOW, e instanceof XEntity ? VisMark.d2 : VisMark.d1)).collect(Collectors.toList());
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
		if(mainState.turnCounter == 0)
		{
			return !character.isStartInvLocked();
		}
		else
		{
			return character.ready(1);
		}
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterGUIMenu(character);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, XKey key)
	{
		boolean levelStarted = mainState.turnCounter > 0;
		List<DoubleInv> list = possibleTargets.stream().filter(e -> mapTile.equals(e.location())).collect(Collectors.toList());
		if(list.isEmpty())
		{
			stateHolder.setState(NoneState.INSTANCE);
		}
		else if(list.size() == 1)
		{
			startTradeState(stateHolder, list.get(0), levelStarted);
		}
		else
		{
			if(mainState.preferBuildings)
			{
				for(DoubleInv inv1 : list)
				{
					if(inv1 instanceof MBuilding)
					{
						startTradeState(stateHolder, inv1, levelStarted);
						break;
					}
				}
			}
			else
			{
				for(DoubleInv inv1 : list)
				{
					if(inv1 instanceof XEntity)
					{
						startTradeState(stateHolder, inv1, levelStarted);
						break;
					}
				}
			}
		}
	}

	private void startTradeState(XStateHolder stateHolder, DoubleInv inv1, boolean levelStarted)
	{
		if(give)
			stateHolder.setState(new DirectedTradeGUI(character, inv1, levelStarted ? character : null));
		else
			stateHolder.setState(new DirectedTradeGUI(inv1, character, levelStarted ? character : null));
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}