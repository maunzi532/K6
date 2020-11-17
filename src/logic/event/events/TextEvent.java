package logic.event.events;

import com.fasterxml.jackson.jr.stree.*;
import logic.*;
import logic.event.*;
import logic.xstate.*;
import text.*;

public class TextEvent implements NEvent, NAutoState
{
	private final CharSequence characterName;
	private final CharSequence text;
	private final boolean r;
	private EventListState eventListState;
	private int counter;

	public TextEvent(JrsObject data)
	{
		if(data.get("Character") != null)
			characterName = new NameText(data.get("Character").asText());
		else
			characterName = null;
		text = data.get("Text").asText();
		r = ((JrsBoolean) data.get("R")).booleanValue();
	}

	@Override
	public void setEventListState(EventListState eventListState)
	{
		this.eventListState = eventListState;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setTextSideInfo(mainState.levelMap().findByName(characterName).orElse(null), text, r);
	}

	@Override
	public void tick(MainState mainState)
	{
		counter++;
	}

	@Override
	public boolean finished()
	{
		return counter >= 20;
	}

	@Override
	public NState nextState()
	{
		return eventListState;
	}
}