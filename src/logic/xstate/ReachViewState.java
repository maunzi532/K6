package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class ReachViewState implements NMarkState
{
	private XEntity character;
	private boolean enemy;
	private List<VisMark> allTargets;

	public ReachViewState(XEntity character, boolean enemy)
	{
		this.character = character;
		this.enemy = enemy;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfoXH(character.standardSideInfo(), character);
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
		return enemy ? "Movement" : "Reach";
	}

	@Override
	public String keybind()
	{
		return enemy ? "View Reach" : "Enemy Reach";
	}

	@Override
	public XMenu menu()
	{
		return enemy ? XMenu.enemyMoveMenu((XEnemy) character) : XMenu.characterStartMoveMenu((XHero) character);
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