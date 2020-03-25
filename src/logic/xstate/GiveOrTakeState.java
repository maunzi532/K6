package logic.xstate;

import building.adv.*;
import doubleinv.*;
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
	public static final Color TARGET_COLOR = Color.YELLOW;

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
		mainState.sideInfoFrame.setStandardSideInfo(character, mainState.colorScheme);
		boolean levelStarted = mainState.turnCounter > 0;
		List<Tile> range = mainState.y1.range(character.location(), 0, character.stats().maxAccessRange());
		possibleTargets = new ArrayList<>();
		range.stream().map(e -> (DoubleInv) mainState.levelMap.getBuilding(e))
				.filter(e -> e != null && e.active() && e.playerTradeable(levelStarted)).forEachOrdered(possibleTargets::add);
		range.stream().map(e -> (DoubleInv) mainState.levelMap.getEntity(e))
				.filter(e -> e != null && e.active() && e.playerTradeable(levelStarted)).forEachOrdered(possibleTargets::add);
		visMarked = possibleTargets.stream().map(e -> new VisMark(e.location(), TARGET_COLOR,
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
		if(mainState.turnCounter == 0)
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
			startTradeState(mainState, stateHolder, list.get(0), levelStarted);
		}
		else
		{
			if(mainState.preferBuildings)
			{
				for(DoubleInv inv1 : list)
				{
					if(inv1 instanceof XBuilding)
					{
						startTradeState(mainState, stateHolder, inv1, levelStarted);
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
						startTradeState(mainState, stateHolder, inv1, levelStarted);
						break;
					}
				}
			}
		}
	}

	private void startTradeState(MainState mainState, XStateHolder stateHolder, DoubleInv inv1, boolean levelStarted)
	{
		if(inv1 == character)
		{
			if(!levelStarted)
			{
				if(give)
					stateHolder.setState(new DirectedTradeGUI(character, mainState.storage, null));
				else
					stateHolder.setState(new DirectedTradeGUI(mainState.storage, character, null));
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