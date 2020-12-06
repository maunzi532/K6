package logic.xstate;

import entity.*;
import java.util.*;
import logic.editor.xgui.*;
import logic.editor.xstate.*;

public final class XMenu
{
	public static final XMenu NOMENU = new XMenu();

	public static XMenu entityEditMenu(XCharacter entity)
	{
		return new XMenu(new EntityEditGUI(entity), new EntityInvEditGUI(entity),
				new EditMoveState(entity), new EditCopyState(entity), new EditDeleteState(entity));
	}

	private final List<NState> entries;

	public XMenu(NState... allEntries)
	{
		entries = List.of(allEntries);
	}

	public List<NState> getEntries()
	{
		return entries;
	}
}