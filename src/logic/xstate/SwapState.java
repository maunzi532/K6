package logic.xstate;

import entity.*;
import entity.sideinfo.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.*;

public class SwapState implements NMarkState
{
	private final XCharacter character;
	private List<XCharacter> swapTargets;
	private List<VisMark> visMarked;

	public SwapState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.setStandardSideInfo(character);
		if(character.saveSettings() != null && character.saveSettings().startLocked)
		{
			swapTargets = List.of();
		}
		else
		{
			swapTargets = levelMap.teamCharacters(character.team()).stream()
					.filter(e -> e != character && (e.saveSettings() == null || !e.saveSettings().startLocked)).collect(Collectors.toList());
		}
		visMarked = swapTargets.stream().map(e -> new VisMark(e.location(), "mark.swap", VisMark.d1)).collect(Collectors.toList());
	}

	@Override
	public String text()
	{
		return "Move";
	}

	@Override
	public String keybind()
	{
		return "Swap";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterStartMoveMenu(character);
	}

	@Override
	public void onClick(MainState mainState, LevelMap levelMap, XStateHolder stateHolder, Tile mapTile, XKey key)
	{
		XCharacter target = levelMap.getEntity(mapTile);
		if(target != null && target.team() == character.team() && swapTargets.contains(target))
		{
			levelMap.swapEntities(character, target);
			visMarked = swapTargets.stream().map(e -> new VisMark(e.location(), "mark.swap", VisMark.d1)).collect(Collectors.toList());
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