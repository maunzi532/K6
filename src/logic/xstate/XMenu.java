package logic.xstate;

import building.*;
import entity.*;
import logic.editor.xgui.*;
import logic.editor.xstate.*;
import logic.gui.guis.*;
import java.util.*;

public class XMenu
{
	public static final XMenu NOMENU = new XMenu();

	public static XMenu characterMoveMenu(XHero character)
	{
		return new XMenu(new AdvMoveState(character), new RegenerateState(character, new AdvMoveState(character)),
				new RevertMovementState(character), new EnemyStartState());
	}

	public static XMenu characterStartMoveMenu(XHero character)
	{
		return new XMenu(new SwapState(character), new ReachViewState(character, false), new StartTurnState());
	}

	public static XMenu characterGUIMenu(XHero character)
	{
		return new XMenu(new CharacterInvGUI(character),
				new GiveOrTakeState(true, character), new GiveOrTakeState(false, character),
				new SelectBuildingGUI(character), new RemoveBuildingGUI(character), new EnemyStartState());
	}

	public static XMenu enemyMoveMenu(XEnemy character)
	{
		return new XMenu(new ReachViewState(character, true), new EnemyStartState());
	}

	public static XMenu enemyGUIMenu(InvEntity enemy)
	{
		return new XMenu(new CharacterInvGUI(enemy), new EnemyStartState());
	}

	public static XMenu productionMenu(ProductionBuilding building)
	{
		return new XMenu(new ProductionFloorsState(building), new RecipeGUI(building),
				new ProductionInvGUI(building));
	}

	public static XMenu transportMenu(Transporter transporter)
	{
		return new XMenu(new TransportTargetsState(transporter), new ProductionPhaseState());
	}

	public static XMenu entityEditMenu(XEntity entity)
	{
		if(entity instanceof InvEntity)
		{
			if(entity instanceof XHero)
			{
				return new XMenu(new EntityEditGUI((InvEntity) entity), new EntityInvEditGUI((InvEntity) entity), new HeroEditGUI((XHero) entity),
						new EditMoveState(entity), new EditCopyState(entity), new EditDeleteState(entity));
			}
			if(entity instanceof XEnemy)
			{
				return new XMenu(new EntityEditGUI((InvEntity) entity), new EntityInvEditGUI((InvEntity) entity), new EnemyEditGUI((XEnemy) entity),
						new EditMoveState(entity), new EditCopyState(entity), new EditDeleteState(entity));
			}
			throw new RuntimeException();
		}
		else
		{
			return new XMenu(new EditMoveState(entity), new EditCopyState(entity), new EditDeleteState(entity));
		}
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