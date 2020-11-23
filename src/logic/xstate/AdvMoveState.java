package logic.xstate;

import entity.*;
import geom.tile.*;
import java.util.*;
import levelmap.*;
import logic.*;
import logic.guis.*;

public final class AdvMoveState implements NMarkState
{
	private final XCharacter character;
	private List<PathLocation> movement;
	private List<Tile> attack;
	private List<Tile> ally;
	private List<VisMark> allTargets;

	public AdvMoveState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		allTargets = new ArrayList<>();
		movement = new ArrayList<>();
		if(character.resources().hasMoveAction())
		{
			movement.addAll(new Pathing(character, character.resources().leftoverMovement(),
					mainState.levelMap(), false).getEndpaths());
			movement.stream().map(e -> new VisMark(e.tile(), "mark.move.move", VisMark.d1)).forEach(allTargets::add);
		}
		attack = new ArrayList<>();
		ally = new ArrayList<>();
		if(character.resources().hasMainAction())
		{
			character.attackRanges().stream().map(e -> mainState.levelMap().y1().range(character.location(), e, e))
					.flatMap(Collection::stream).map(mainState.levelMap()::getEntity).filter(e -> e != null && e.targetable() && e.team() != character.team())
					.forEach(e -> attack.add(e.location()));
			attack.stream().map(e -> new VisMark(e, "mark.move.attack", VisMark.d1)).forEach(allTargets::add);
			character.allyRanges().stream().map(e -> mainState.levelMap().y1().range(character.location(), e, e))
					.flatMap(Collection::stream).map(mainState.levelMap()::getEntity).filter(e -> e != null && e.targetable() && e.team() == character.team())
					.forEach(e -> attack.add(e.location()));
			ally.stream().map(e -> new VisMark(e, "mark.move.ally", VisMark.d1)).forEach(allTargets::add);
		}
	}

	@Override
	public CharSequence text()
	{
		return "menu.advmove";
	}

	@Override
	public String keybind()
	{
		return "state.advmove";
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
			//mainState.stateHolder().setState(new AttackInfoGUI(character, mainState.levelMap().getEntity(mapTile)));
			//mainState.stateHolder().setState(new AttackAnimState(NoneState.INSTANCE, character, mainState.levelMap().getEntity(mapTile)));
			mainState.stateHolder().setState(new AttackInfoGUI4(character, mainState.levelMap().getEntity(mapTile)));
		}
		else if(ally.contains(mapTile))
		{
			//TODO
			//mainState.stateHolder().setState(new AttackInfoGUI(character, mainState.levelMap().getEntity(mapTile)));
		}
		else
		{
			Optional<PathLocation> pathLocation = movement.stream().filter(e -> e.tile().equals(mapTile)).findFirst();
			if(pathLocation.isPresent())
			{
				if(character.resources().hasMoveAction())
				{
					character.resources().move(pathLocation.get().cost());
					mainState.levelMap().moveEntity(character, mapTile);
					mainState.stateHolder().setState(new AdvMoveState(character));
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