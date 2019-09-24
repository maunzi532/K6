package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class SwapState implements NMarkState
{
	private XHero character;
	private List<XHero> swapTargets;
	private List<VisMark> visMarked;

	public SwapState(XHero character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfo(character.standardSideInfo(), null);
		if(character.isStartLocked())
		{
			swapTargets = List.of();
		}
		else
		{
			swapTargets = mainState.levelMap.getEntitiesH().stream()
					.filter(e -> e != character && !e.isStartLocked()).collect(Collectors.toList());
		}
		visMarked = swapTargets.stream().map(e -> new VisMark(e.location(), Color.YELLOW, VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public String text()
	{
		return "Move";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.M;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterStartMoveMenu(character);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, int key)
	{
		XEntity entity = mainState.levelMap.getEntity(mapTile);
		if(entity instanceof XHero && swapTargets.contains(entity))
		{
			mainState.levelMap.swapEntities(character, entity);
		}
		else
		{
			stateHolder.setState(NoneState.INSTANCE);
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}