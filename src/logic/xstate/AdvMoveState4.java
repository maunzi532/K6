package logic.xstate;

import entity.*;
import geom.tile.*;
import java.util.*;
import levelmap.*;
import logic.*;
import logic.guis.*;

public final class AdvMoveState4 implements NMarkState
{
	private final XCharacter character;
	private final MoveState moveState;
	private final Tile startLocation;
	private MainState mainState1;
	private List<PathLocation> movement;
	private List<XCharacter> attack;
	private List<XCharacter> ally;
	private List<VisMark> allTargets;

	public AdvMoveState4(XCharacter character)
	{
		this.character = character;
		moveState = MoveState.INIT;
		startLocation = character.location();
	}

	public AdvMoveState4(XCharacter character, MoveState moveState, Tile startLocation)
	{
		this.character = character;
		this.moveState = moveState;
		this.startLocation = startLocation;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState1 = mainState;
		mainState.side().setStandardSideInfo(character);
		switch(moveState)
		{
			case INIT, MOVED -> {}
			case ACTION_USED, MOVED_LOCKED -> character.setHasMainAction(false);
		}
		LevelMap4 levelMap = mainState.levelMap();
		allTargets = new ArrayList<>();
		movement = new ArrayList<>();
		if(moveState.canMove)
		{
			movement.addAll(new Pathing(character, character.movement(), levelMap, false).getEndpaths());
			movement.stream().map(e -> new VisMark(e.tile(), "mark.move.move", VisMark.d1)).forEach(allTargets::add);
		}
		attack = new ArrayList<>();
		ally = new ArrayList<>();
		if(moveState.canUseMainAction)
		{
			character.enemyTargetRanges(false).stream()
					.flatMap(e -> levelMap.y1().range(character.location(), e, e).stream())
					.map(levelMap::getEntity).filter(e -> e != null && e.targetable() && e.team() != character.team())
					.forEach(e -> attack.add(e));
			attack.stream().map(e -> new VisMark(e.location(), "mark.move.attack", VisMark.d1)).forEach(allTargets::add);
			character.allyTargetRanges().stream()
					.flatMap(e -> levelMap.y1().range(character.location(), e, e).stream())
					.map(levelMap::getEntity).filter(e -> e != null && e.targetable() && e.team() == character.team())
					.forEach(e -> ally.add(e));
			ally.stream().map(e -> new VisMark(e.location(), "mark.move.ally", VisMark.d1)).forEach(allTargets::add);
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
		return XMenu.characterMoveMenu4(character);
	}

	@Override
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		Optional<XCharacter> v1 = attack.stream().filter(e -> e.location().equals(mapTile)).findFirst();
		if(v1.isPresent())
		{
			onClickAttack(v1.orElseThrow());
			return;
		}
		Optional<XCharacter> v2 = ally.stream().filter(e -> e.location().equals(mapTile)).findFirst();
		if(v2.isPresent())
		{
			onClickAlly(v2.orElseThrow());
			return;
		}
		Optional<PathLocation> v3 = movement.stream().filter(e -> e.tile().equals(mapTile)).findFirst();
		if(v3.isPresent())
		{
			onClickMovement(v3.orElseThrow());
			return;
		}
		onEscape(mainState.stateHolder());
	}

	private StateReverter actionStateReverter()
	{
		return switch(moveState)
				{
					case INIT -> new StateReverter(NoneState.INSTANCE, NoneState.INSTANCE, new AdvMoveState4(character, MoveState.ACTION_USED, startLocation));
					case ACTION_USED -> new StateReverter(new AdvMoveState4(character, MoveState.ACTION_USED, startLocation), new AdvMoveState4(character, MoveState.ACTION_USED, startLocation), new AdvMoveState4(character, MoveState.ACTION_USED, startLocation));
					case MOVED -> new StateReverter(new AdvMoveState4(character, MoveState.MOVED, startLocation), new AdvMoveState4(character, MoveState.MOVED_LOCKED, startLocation), new EndMoveState(character));
					case MOVED_LOCKED -> new StateReverter(new AdvMoveState4(character, MoveState.MOVED_LOCKED, startLocation), new AdvMoveState4(character, MoveState.MOVED_LOCKED, startLocation), new EndMoveState(character));
				};
	}

	private void onClickAttack(XCharacter target)
	{
		mainState1.stateHolder().setState(new AttackInfoGUI4(character, target, actionStateReverter()));
	}

	private void onClickAlly(XCharacter target)
	{
		//mainState1.stateHolder().setState(new AttackInfoGUI4(character, target, actionStateReverter()));
	}

	private void onClickMovement(PathLocation target)
	{
		switch(moveState)
		{
			case INIT ->
					{
						mainState1.levelMap().moveEntity(character, target.tile());
						mainState1.stateHolder().setState(new AdvMoveState4(character, MoveState.MOVED, startLocation));
					}
			case ACTION_USED ->
					{
						mainState1.levelMap().moveEntity(character, target.tile());
						mainState1.stateHolder().setState(new EndMoveState(character));
					}
		}
	}

	@Override
	public void onEscape(XStateHolder stateHolder)
	{
		switch(moveState)
		{
			case INIT -> stateHolder.setState(NoneState.INSTANCE);
			case MOVED ->
					{
						mainState1.levelMap().moveEntity(character, startLocation);
						stateHolder.setState(NoneState.INSTANCE);
					}
			case ACTION_USED, MOVED_LOCKED -> {}
		}
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return allTargets;
	}

	private enum MoveState
	{
		INIT(true, true),
		ACTION_USED(true, false),
		MOVED(false, true),
		MOVED_LOCKED(false, true);

		public final boolean canMove;
		public final boolean canUseMainAction;

		MoveState(boolean canMove, boolean canUseMainAction)
		{
			this.canMove = canMove;
			this.canUseMainAction = canUseMainAction;
		}
	}
}