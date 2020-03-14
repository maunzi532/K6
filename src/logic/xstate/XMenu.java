package logic.xstate;

import building.adv.*;
import building.transport.*;
import entity.*;
import java.util.*;
import logic.editor.xgui.*;
import logic.editor.xstate.*;
import logic.gui.guis.*;

public class XMenu
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
		return new XMenu(new CharacterInvGUI(character), new CharacterCombatGUI(character, 0),
				new GiveOrTakeState(true, character), new GiveOrTakeState(false, character),
				new SelectBuildingGUI(character), new RemoveBuildingGUI(character), new EndTurnState());
	}

	public static XMenu enemyMoveMenu(XCharacter character)
	{
		return new XMenu(new ReachViewState(character, true), new EndTurnState());
	}

	public static XMenu enemyGUIMenu(XCharacter enemy)
	{
		return new XMenu(new CharacterInvGUI(enemy), new EndTurnState());
	}

	public static XMenu productionMenu(XBuilding building, ProcessInv processInv)
	{
		return new XMenu(new ProductionFloorsState(building, processInv), new RecipeGUI(building, processInv),
				new ProductionInvGUI(building, processInv));
	}

	public static XMenu transportMenu(XBuilding building, Transport transport)
	{
		return new XMenu(new TransportTargetsState(building, transport), new ProductionPhaseState());
	}

	public static XMenu entityEditMenu(XCharacter entity)
	{
		return new XMenu(new EntityEditGUI(entity), new EntityInvEditGUI(entity),
				new SaveSettingsEditGUI(entity), new EntityEditGUI(entity),
				new EditMoveState(entity), new EditCopyState(entity), new EditDeleteState(entity));
	}

	public static XMenu buildingEditMenu(XBuilding building)
	{
		return new XMenu(new BuildingInvEditGUI(building, false),
				new BuildingInvEditGUI(building, true));
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