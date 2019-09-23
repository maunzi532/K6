package logic.xstate;

import entity.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.*;

public class AdvMoveState implements NMarkState
{
	private XHero character;
	private Map<Tile, MarkType> markMap;
	private List<VisMark> movementTargets;
	private List<VisMark> attackTargets;
	private List<VisMark> allTargets;

	public AdvMoveState(XHero character)
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
		mainState.sideInfoFrame.setSideInfo(character.standardSideInfo(), null);
		markMap = new HashMap<>();
		movementTargets = new ArrayList<>();
		attackTargets = new ArrayList<>();
		if(character.canMove())
		{
			new Pathing(mainState.y2, character, character.movement(), mainState.levelMap, null)
					.start().getEndpoints()
					.forEach(e ->
					{
						markMap.put(e, MarkType.TARGET);
						movementTargets.add(new VisMark(e, Color.YELLOW, VisMark.d1));
					});
		}
		if(character.ready(2))
		{
			character.attackRanges(false).stream().map(e -> mainState.y2.range(character.location(), e, e))
					.flatMap(Collection::stream).map(mainState.levelMap::getEntity).filter(e -> character.isEnemy(e))
					.forEach(e ->
					{
						markMap.put(e.location(), MarkType.ON);
						attackTargets.add(new VisMark(e.location(), Color.RED, VisMark.d1));
					});
		}
		allTargets = new ArrayList<>();
		allTargets.addAll(movementTargets);
		allTargets.addAll(attackTargets);
	}

	@Override
	public Map<Tile, MarkType> marked(LevelMap levelMap)
	{
		return markMap;
	}

	@Override
	public void onClickMarked(Tile mapTile, MarkType markType, int key, LevelMap levelMap, XStateHolder stateHolder)
	{
		if(markType == MarkType.TARGET)
		{
			character.setMoved();
			levelMap.moveEntity(character, mapTile);
			stateHolder.setState(new AdvMoveState(character));
		}
		else if(markType == MarkType.ON)
		{
			stateHolder.setState(new AttackInfoState(character, levelMap.getEntity(mapTile)));
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}
}