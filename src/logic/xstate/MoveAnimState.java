package logic.xstate;

import arrow.*;
import entity.*;
import geom.f1.*;
import logic.*;

public class MoveAnimState implements NAutoState
{
	private final NState nextState;
	private final XCharacter entity;
	private final Tile newLocation;
	private int counter;
	private int finish;

	public MoveAnimState(NState nextState, XCharacter entity, Tile newLocation)
	{
		this.nextState = nextState;
		this.entity = entity;
		this.newLocation = newLocation;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		finish = (mainState.levelMap().y1.distance(newLocation, entity.location()) + 1) * XArrow.TIME_PER_DISTANCE;
		mainState.levelMap().moveEntity(entity, newLocation);
	}

	@Override
	public void tick(MainState mainState)
	{
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= finish;
	}

	@Override
	public NState nextState()
	{
		return nextState;
	}
}