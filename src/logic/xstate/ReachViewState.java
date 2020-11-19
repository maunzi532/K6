package logic.xstate;

import entity.*;
import geom.tile.*;
import java.util.*;
import levelmap.*;
import logic.*;

public final class ReachViewState implements NMarkState
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
		mainState.side().setStandardSideInfo(character);
		List<Tile> movement = new Pathing(mainState.levelMap().y1, character, character.movement(), mainState.levelMap(), null).start().getEndpoints();
		allTargets = new ArrayList<>();
		movement.forEach(e -> allTargets.add(new VisMark(e, "mark.reach.move", VisMark.d1)));
		movement.stream().flatMap(loc -> character.attackRanges().stream()
				.flatMap(e -> mainState.levelMap().y1.range(loc, e, e).stream())).distinct()
				.forEach(e -> allTargets.add(new VisMark(e, "mark.reach.attack", VisMark.d1)));
	}

	@Override
	public CharSequence text()
	{
		return enemy ? "menu.reach.enemy" : "menu.reach.ally";
	}

	@Override
	public String keybind()
	{
		return enemy ? "state.reach.enemy" : "state.reach.ally";
	}

	@Override
	public XMenu menu()
	{
		return enemy ? XMenu.enemyMoveMenu(character) : XMenu.characterStartMoveMenu(character);
	}

	@Override
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		mainState.stateHolder().setState(NoneState.INSTANCE);
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}
}