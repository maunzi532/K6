package logic.xstate;

import entity.*;
import entity.sideinfo.*;
import geom.f1.*;
import java.util.*;
import levelMap.*;
import logic.*;
import logic.gui.guis.*;

public class AdvMoveState implements NMarkState
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
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.setStandardSideInfo(character);
		movement = new ArrayList<>();
		attack = new ArrayList<>();
		if(character.resources().moveAction())
		{
			movement.addAll(new Pathing(levelMap.y1, character, character.resources().movement(),
					levelMap, null).start().getEndpaths());
		}
		else if(character.resources().ready(2) && character.resources().dashMovement() > 0)
		{
			movement.addAll(new Pathing(levelMap.y1, character, character.resources().dashMovement(),
					levelMap, null).start().getEndpaths());
		}
		if(character.resources().ready(2))
		{
			levelMap
					.attackRanges(character, false).stream().map(e -> levelMap.y1.range(character.location(), e, e))
					.flatMap(Collection::stream).map(levelMap::getEntity).filter(e -> e != null && e.targetable() && e.team() != character.team())
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
	public void onClick(MainState mainState, LevelMap levelMap, XStateHolder stateHolder, Tile mapTile, XKey key)
	{
		if(attack.contains(mapTile))
		{
			stateHolder.setState(new AttackInfoGUI(character, levelMap.getEntity(mapTile)));
		}
		else
		{
			Optional<PathLocation> pathLocation = movement.stream().filter(e -> e.tile().equals(mapTile)).findFirst();
			if(pathLocation.isPresent())
			{
				if(character.resources().moveAction())
				{
					character.resources().move(pathLocation.get().cost());
					levelMap.moveEntity(character, mapTile);
					stateHolder.setState(new AdvMoveState(character));
				}
				else
				{
					character.resources().move(pathLocation.get().cost());
					character.resources().action(true, 2);
					levelMap.moveEntity(character, mapTile);
					stateHolder.setState(NoneState.INSTANCE);
				}
			}
			else
			{
				stateHolder.setState(NoneState.INSTANCE);
			}
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}
}