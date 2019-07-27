package logic.xstate;

import entity.*;
import geom.f1.*;
import levelMap.*;
import logic.*;

public class MoveAnimState implements NAutoState
{
	private NState nextState;
	private XEntity entity;
	private Tile newLocation;
	private int counter;
	private int finish;

	public MoveAnimState(NState nextState, XEntity entity, Tile newLocation)
	{
		this.nextState = nextState;
		this.entity = entity;
		this.newLocation = newLocation;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		finish = (mainState.y2.distance(newLocation, entity.location()) + 1) * LevelMap.TIME_PER_DISTANCE;
		mainState.levelMap.moveEntity(entity, newLocation);
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