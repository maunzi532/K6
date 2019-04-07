package logic.xstate;

import building.*;
import entity.*;
import java.util.*;

public class XMenu
{
	public static final XMenu NOMENU = new XMenu();

	public static XMenu characterMenu(XHero character)
	{
		return new XMenu(new CharacterMovementState(character),
				new CharacterInvState(character), new AttackTargetState(character),
				new GiveOrTakeState(true, character), new GiveOrTakeState(false, character),
				new BuildingChooseState(character), new RemoveBuildingState(character));
	}

	public static XMenu productionMenu(ProductionBuilding building)
	{
		return new XMenu(new ProductionFloorsState(building), new ProductionViewState(building),
				new ProductionInvState(building), ProductionPhaseState.INSTANCE, TransportPhaseState.INSTANCE);
	}

	public static XMenu transportMenu(Transporter transporter)
	{
		return new XMenu(new TransportTargetsState(transporter), ProductionPhaseState.INSTANCE, TransportPhaseState.INSTANCE);
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