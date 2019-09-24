package logic.xstate;

import entity.*;
import geom.f1.*;
import logic.gui.guis.*;
import java.util.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class AdvMoveState implements NMarkState
{
	private XHero character;
	private List<Tile> movement;
	private List<Tile> attack;
	private List<VisMark> allTargets;

	public AdvMoveState(XHero character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfo(character.standardSideInfo(), null);
		movement = new ArrayList<>();
		attack = new ArrayList<>();
		if(character.canMove())
		{
			movement.addAll(new Pathing(mainState.y1, character, character.movement(), mainState.levelMap, null).start().getEndpoints());
		}
		if(character.ready(2))
		{
			character.attackRanges(false).stream().map(e -> mainState.y1.range(character.location(), e, e))
					.flatMap(Collection::stream).map(mainState.levelMap::getEntity).filter(e -> character.isEnemy(e))
					.forEach(e -> attack.add(e.location()));
		}
		allTargets = new ArrayList<>();
		movement.stream().map(e -> new VisMark(e, Color.YELLOW, VisMark.d1)).forEach(allTargets::add);
		attack.stream().map(e -> new VisMark(e, Color.RED, VisMark.d1)).forEach(allTargets::add);
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
	public void onClick(Tile mapTile, MainState mainState, XStateHolder stateHolder, int key)
	{
		if(attack.contains(mapTile))
		{
			stateHolder.setState(new AttackInfoGUI(character, mainState.levelMap.getEntity(mapTile)));
		}
		else if(movement.contains(mapTile))
		{
			character.setMoved();
			mainState.levelMap.moveEntity(character, mapTile);
			stateHolder.setState(new AdvMoveState(character));
		}
		else
		{
			stateHolder.setState(NoneState.INSTANCE);
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}
}