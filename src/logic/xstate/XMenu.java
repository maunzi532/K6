package logic.xstate;

import building.*;
import entity.*;
import java.util.*;
import logic.*;

public class XMenu
{
	public static final XMenu NOMENU = new XMenu();

	public static XMenu characterMenu(XHero character)
	{
		return new XMenu(new AdvMoveState(character), new CharacterInvState(character),
				new RegenerateState(character, new AdvMoveState(character)),
				new GiveOrTakeState(true, character), new GiveOrTakeState(false, character),
				new RevertMovementState(character),
				new BuildingChooseState(character), new RemoveBuildingState(character), new EnemyStartState());
	}

	public static XMenu enemyMenu(InvEntity enemy)
	{
		return new XMenu(new CharacterInvState(enemy), new EnemyStartState());
	}

	public static XMenu productionMenu(ProductionBuilding building)
	{
		return new XMenu(new ProductionFloorsState(building), new ProductionViewState(building),
				new ProductionInvState(building));
	}

	public static XMenu transportMenu(Transporter transporter)
	{
		return new XMenu(new TransportTargetsState(transporter), new ProductionPhaseState());
	}

	public static XMenu entityEditMenu(XEntity entity, MainState mainState)
	{
		return new XMenu(new EntityEditState(entity, mainState), new EntityInvEditState(entity, mainState),
				new EditMoveState(entity, mainState), new EditCopyState(entity, mainState), new EditDeleteState(entity, mainState));
	}

	private List<NState> entries;

	public XMenu(NState... allEntries)
	{
		entries = List.of(allEntries);
	}

	public List<NState> getEntries()
	{
		return entries;
	}
}