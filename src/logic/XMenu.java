package logic;

import java.util.*;

public class XMenu
{
	private List<XMenuEntry> entries;
	private XMenuEntry current;

	public XMenu()
	{
		entries = List.of();
	}

	public List<XMenuEntry> getEntries()
	{
		return entries;
	}

	public XMenuEntry getCurrent()
	{
		return current;
	}

	public void setCurrent(XMenuEntry current)
	{
		this.current = current;
	}

	public void updateState(XState state)
	{
		entries = switch(state)
		{
			case ENTITY -> List.of(XMenuEntry.CHARACTER_MOVEMENT);
			case BUILDING -> List.of(XMenuEntry.BUILDING_VIEW, XMenuEntry.PRODUCTION_PHASE, XMenuEntry.TRANSPORT_PHASE);
			default -> List.of();
		};
		current = switch(state)
		{
			case ENTITY -> XMenuEntry.CHARACTER_MOVEMENT;
			case BUILDING -> XMenuEntry.BUILDING_VIEW;
			default -> XMenuEntry.DUMMY;
		};
	}
}