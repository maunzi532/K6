package event.event;

import java.util.*;
import logic.*;
import xstate.*;

public final class EventListState implements NAutoState
{
	private final List<? extends NEvent> events;
	private final NState nextState;
	private int num;
	private NState nextStateNow;

	public EventListState(List<? extends NEvent> events, NState nextState)
	{
		this.events = events;
		this.nextState = nextState;
		num = 0;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		if(num >= events.size())
		{
			nextStateNow = nextState;
		}
		else
		{
			NEvent event1 = events.get(num);
			event1.setEventListState(this);
			nextStateNow = event1;
			num++;
		}
	}

	@Override
	public void tick(MainState mainState){}

	@Override
	public boolean finished()
	{
		return true;
	}

	@Override
	public NState nextState()
	{
		return nextStateNow;
	}
}