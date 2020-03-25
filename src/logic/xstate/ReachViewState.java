package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import levelMap.*;
import logic.*;

public class ReachViewState implements NMarkState
{
	private final XCharacter character;
	private final boolean enemy;
	private List<VisMark> allTargets;

	public ReachViewState(XCharacter character, boolean enemy)
	{
		this.character = character;
		this.enemy = enemy;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setStandardSideInfo(character);
		List<Tile> movement = new Pathing(mainState.y1, character, character.stats().movement(), mainState.levelMap, null).start().getEndpoints();
		allTargets = new ArrayList<>();
		movement.forEach(e -> allTargets.add(new VisMark(e, "mark.reach.move", VisMark.d1)));
		movement.stream().flatMap(loc -> mainState.levelMap.attackRanges(character, false).stream()
				.flatMap(e -> mainState.y1.range(loc, e, e).stream())).distinct()
				.forEach(e -> allTargets.add(new VisMark(e, "mark.reach.attack", VisMark.d1)));
	}

	@Override
	public String text()
	{
		return enemy ? "Movement" : "Reach";
	}

	@Override
	public String keybind()
	{
		return enemy ? "Enemy Reach" : "View Reach";
	}

	@Override
	public XMenu menu()
	{
		return enemy ? XMenu.enemyMoveMenu(character) : XMenu.characterStartMoveMenu(character);
	}

	@Override
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, XKey key)
	{
		stateHolder.setState(NoneState.INSTANCE);
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}
}