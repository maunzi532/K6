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
	private List<VisMark> movementRange;
	private List<VisMark> attackRange;
	private List<VisMark> allTargets;

	public ReachViewState(XEnemy character)
	{
		this.character = character;
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
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.setSideInfo(null, character.standardSideInfo());
		movementRange = new ArrayList<>();
		attackRange = new ArrayList<>();
		List<Tile> movement =
				new Pathing(mainState.y2, character, character.movement(), mainState.levelMap, null)
				.start().getEndpoints();
		movement.forEach(e -> movementRange.add(new VisMark(e, Color.MEDIUMVIOLETRED, VisMark.d1)));
		movement.stream().map(loc -> character.attackRanges(false).stream()
				.map(e -> mainState.y2.range(loc, e, e))
				.flatMap(Collection::stream)).flatMap(e -> e).distinct()
				.forEach(e -> attackRange.add(new VisMark(e, Color.RED, VisMark.d1)));
		allTargets = new ArrayList<>();
		allTargets.addAll(attackRange);
		allTargets.addAll(movementRange);
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateHolder stateHolder){}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return Map.of();
	}
}