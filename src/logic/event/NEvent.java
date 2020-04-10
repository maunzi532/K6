package logic.event;

import logic.xstate.*;

public interface NEvent extends NState
{
	void setEventListState(EventListState eventListState);
}