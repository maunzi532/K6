package logic.xstate;

import entity.*;
import geom.f1.*;
import building.transport.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

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
		mainState.sideInfoFrame.setSideInfo(character.standardSideInfo(), null);
		possibleTargets = mainState.y1.range(character.location(), 0, character.maxAccessRange()).stream()
				.filter(e -> DoubleInv.isTargetable(mainState.levelMap.getBuilding(e)))
				.map(e -> (DoubleInv) mainState.levelMap.getBuilding(e)).collect(Collectors.toList());
		visMarked = possibleTargets.stream().map(e -> new VisMark(e.location(), Color.YELLOW, VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public String text()
	{
		return give ? "Give" : "Take";
	}

	@Override
	public KeyCode keybind()
	{
		return give ? KeyCode.G : KeyCode.T;
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return character.ready(1);
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterGUIMenu(character);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, int key)
	{
		List<DoubleInv> list = possibleTargets.stream().filter(e -> mapTile.equals(e.location())).collect(Collectors.toList());
		if(list.isEmpty())
		{
			stateHolder.setState(NoneState.INSTANCE);
		}
		else
		{
			for(DoubleInv inv1 : list)
			{
				if(give)
					stateHolder.setState(new DirectedTradeState(character, inv1, character));
				else
					stateHolder.setState(new DirectedTradeState(inv1, character, character));
			}
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}