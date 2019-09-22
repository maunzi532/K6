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
	private Map<Tile, MarkType> markMap;
	private List<XHero> swapTargets;
	private List<VisMark> visMarked;

	public SwapState(XHero character)
	{
		this.character = character;
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
		return XMenu.characterMoveMenu(character);
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.setSideInfo(character.standardSideInfo(), null);
		if(character.isStartLocked())
		{
			swapTargets = List.of();
		}
		else
		{
			swapTargets = mainState.levelMap.getEntitiesH().stream()
					.filter(e -> e != character && !e.isStartLocked()).collect(Collectors.toList());
		}
		markMap = new HashMap<>();
		swapTargets.forEach(e -> markMap.put(e.location(), MarkType.TARGET));
		visMarked = swapTargets.stream().map(e -> new VisMark(e.location(), Color.YELLOW, VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return markMap;
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateHolder stateHolder)
	{
		levelMap.swapEntities(character, levelMap.getEntity(mapTile));
		stateHolder.setState(new SwapState(character));
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}