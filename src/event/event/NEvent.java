package event.event;

import xstate.*;

public interface NEvent extends NState
{
	void setEventListState(EventListState eventListState);
}