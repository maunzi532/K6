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
			case BUILDING -> List.of(XMenuEntry.PRODUCTION_VIEW, XMenuEntry.PRODUCTION_PHASE);
			case TRANSPORTER -> List.of(XMenuEntry.TRANSPORT_VIEW, XMenuEntry.EDIT_TARGETS, XMenuEntry.TRANSPORT_PHASE);
			default -> List.of();
		};
		current = switch(state)
		{
			case ENTITY -> XMenuEntry.CHARACTER_MOVEMENT;
			case BUILDING -> XMenuEntry.PRODUCTION_VIEW;
			case TRANSPORTER -> XMenuEntry.TRANSPORT_VIEW;
			default -> XMenuEntry.DUMMY;
		};
	}
}