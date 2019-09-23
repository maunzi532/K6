package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class ReachViewState implements NMarkState
{
	private XEnemy character;
	private List<VisMark> allTargets;

	public ReachViewState(XEnemy character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfo(null, character.standardSideInfo());
		List<Tile> movement = new Pathing(mainState.y1, character, character.movement(), mainState.levelMap, null).start().getEndpoints();
		allTargets = new ArrayList<>();
		movement.forEach(e -> allTargets.add(new VisMark(e, Color.MEDIUMVIOLETRED, VisMark.d1)));
		movement.stream().flatMap(loc -> character.attackRanges(false).stream()
				.flatMap(e -> mainState.y1.range(loc, e, e).stream())).distinct()
				.forEach(e -> allTargets.add(new VisMark(e, Color.RED, VisMark.d1)));
	}

	@Override
	public String text()
	{
		return "Movement";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.M;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.enemyMoveMenu(character);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, int key)
	{
		stateHolder.setState(NoneState.INSTANCE);
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}

}