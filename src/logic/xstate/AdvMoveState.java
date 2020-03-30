package logic.xstate;

import entity.*;
import geom.tile.*;
import java.util.*;
import levelmap.*;
import logic.*;
import logic.gui.guis.*;
import statsystem.*;

public final class AdvMoveState implements NMarkState
{
	private final XCharacter character;
	private List<PathLocation> movement;
	private List<Tile> attack;
	private List<VisMark> allTargets;

	public AdvMoveState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		movement = new ArrayList<>();
		if(character.resources().moveAction())
		{
			movement.addAll(new Pathing(mainState.levelMap().y1, character, character.resources().movement(),
					mainState.levelMap(), null).start().getEndpaths());
		}
		else if(character.resources().ready(2) && character.resources().dashMovement() > 0)
		{
			movement.addAll(new Pathing(mainState.levelMap().y1, character, character.resources().dashMovement(),
					mainState.levelMap(), null).start().getEndpaths());
		}
		attack = new ArrayList<>();
		if(character.resources().ready(2))
		{
			LevelMap.attackRanges(character, AttackSide.INITIATOR).stream().map(e -> mainState.levelMap().y1.range(character.location(), e, e))
					.flatMap(Collection::stream).map(mainState.levelMap()::getEntity).filter(e -> e != null && e.targetable() && e.team() != character.team())
					.forEach(e -> attack.add(e.location()));
		}
		allTargets = new ArrayList<>();
		if(character.resources().moveAction())
			movement.stream().map(e -> new VisMark(e.tile(), "mark.move.move", VisMark.d1)).forEach(allTargets::add);
		else
			movement.stream().map(e -> new VisMark(e.tile(), "mark.move.dash", VisMark.d1)).forEach(allTargets::add);
		attack.stream().map(e -> new VisMark(e, "mark.move.attack", VisMark.d1)).forEach(allTargets::add);
	}

	@Override
	public String text()
	{
		return "Move";
	}

	@Override
	public String keybind()
	{
		return "Movement";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMoveMenu(character);
	}

	@Override
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		if(attack.contains(mapTile))
		{
			mainState.stateHolder().setState(new AttackInfoGUI(character, mainState.levelMap().getEntity(mapTile)));
		}
		else
		{
			Optional<PathLocation> pathLocation = movement.stream().filter(e -> e.tile().equals(mapTile)).findFirst();
			if(pathLocation.isPresent())
			{
				if(character.resources().moveAction())
				{
					character.resources().move(pathLocation.get().cost());
					mainState.levelMap().moveEntity(character, mapTile);
					mainState.stateHolder().setState(new AdvMoveState(character));
				}
				else
				{
					character.resources().move(pathLocation.get().cost());
					character.resources().action(true, 2);
					mainState.levelMap().moveEntity(character, mapTile);
					mainState.stateHolder().setState(NoneState.INSTANCE);
				}
			}
			else
			{
				mainState.stateHolder().setState(NoneState.INSTANCE);
			}
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}
}