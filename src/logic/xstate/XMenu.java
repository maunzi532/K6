package logic.xstate;

import doubleinv.*;
import entity.*;
import java.util.*;
import logic.editor.xgui.*;
import logic.editor.xstate.*;
import logic.gui.guis.*;

public final class XMenu
{
	public static final XMenu NOMENU = new XMenu();

	public static XMenu characterMoveMenu(XCharacter character)
	{
		return new XMenu(new AdvMoveState(character), new RegenerateState(character, new AdvMoveState(character)),
				new RevertMovementState(character), new EndTurnState());
	}

	public static XMenu characterStartMoveMenu(XCharacter character)
	{
		return new XMenu(new SwapState(character), new ReachViewState(character, false), new EndTurnState());
	}

	public static XMenu characterGUIMenu(XCharacter character)
	{
		return new XMenu(new CharacterCombatGUI(character, 0),
				new GiveOrTakeState(TradeDirection.GIVE, character), new GiveOrTakeState(TradeDirection.TAKE, character), new EndTurnState());
	}

	public static XMenu enemyMoveMenu(XCharacter character)
	{
		return new XMenu(new ReachViewState(character, true), new EndTurnState());
	}

	public static XMenu enemyGUIMenu(XCharacter enemy)
	{
		return new XMenu(new CharacterCombatGUI(enemy, 0), new EndTurnState());
	}

	public static XMenu entityEditMenu(XCharacter entity)
	{
		return new XMenu(new EntityEditGUI(entity), new EntityInvEditGUI(entity), new StartingSettingsEditGUI(entity),
				new EditMoveState(entity), new EditCopyState(entity), new EditDeleteState(entity));
	}

	private final List<NState> entries;

	private XMenu(NState... allEntries)
	{
		entries = List.of(allEntries);
	}

	public List<NState> getEntries()
	{
		return entries;
	}
}