package xstate;

import entity.*;
import geom.tile.*;
import java.util.*;
import levelmap.*;
import logic.*;

public final class ReachViewState implements NMarkState
{
	private final XCharacter character;
	private MainState mainState1;
	private List<VisMark> allTargets;

	public ReachViewState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState1 = mainState;
		mainState.side().setStandardSideInfo(character);
		List<Tile> movement = new Pathing(character, character.movement(), mainState.levelMap(), true).getEndpoints();
		allTargets = new ArrayList<>();
		movement.forEach(e -> allTargets.add(new VisMark(e, "mark.reach.move", VisMark.d1)));
		movement.stream().flatMap(loc -> character.enemyTargetRanges(character.team() == CharacterTeam.ENEMY).stream()
				.flatMap(e -> mainState.levelMap().y1().range(loc, e, e).stream())).distinct()
				.forEach(e -> allTargets.add(new VisMark(e, "mark.reach.attack", VisMark.d1)));
	}

	@Override
	public CharSequence text()
	{
		return "menu.reach";
	}

	@Override
	public String keybind()
	{
		return "state.reach";
	}

	@Override
	public XMenu menu()
	{
		if(character.team() == CharacterTeam.HERO)
		{
			if(mainState1.levelMap().levelStarted())
				return new XMenu(this, new EndTurnState());
			else
				return new XMenu(new SwapState(character), this, new EndTurnState());
		}
		else
		{
			return new XMenu(this, new EndTurnState());
		}
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