package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class SwapState implements NMarkState
{
	public static final Color TARGET = Color.YELLOW;
	private final XCharacter character;
	private List<XCharacter> swapTargets;
	private List<VisMark> visMarked;

	public SwapState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setStandardSideInfo(character, mainState.colorScheme);
		if(character.saveSettings() != null && character.saveSettings().startLocked)
		{
			swapTargets = List.of();
		}
		else
		{
			swapTargets = mainState.levelMap.teamCharacters(character.team()).stream()
					.filter(e -> e != character && (e.saveSettings() == null || !e.saveSettings().startLocked)).collect(Collectors.toList());
		}
		visMarked = swapTargets.stream().map(e -> new VisMark(e.location(), TARGET, VisMark.d1)).collect(Collectors.toList());
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
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, XKey key)
	{
		XCharacter target = mainState.levelMap.getEntity(mapTile);
		if(target != null && target.team() == character.team() && swapTargets.contains(target))
		{
			mainState.levelMap.swapEntities(character, target);
			visMarked = swapTargets.stream().map(e -> new VisMark(e.location(), TARGET, VisMark.d1)).collect(Collectors.toList());
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