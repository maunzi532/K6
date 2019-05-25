package entity;

import building.*;
import building.blueprint.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.util.*;
import javafx.scene.image.*;
import logic.*;
import logic.xstate.*;

public class XHero extends InvEntity
{
	private static final Image IMAGE_S = new Image("S.png");

	private boolean canMove;
	private int ap;
	private boolean mainAction;
	private Tile revertLocation;
	private boolean canRevert;

	public XHero(Tile location, MainState mainState, Stats stats, int weightLimit, ItemList itemList)
	{
		super(location, mainState, stats, weightLimit, itemList);
	}

	public XHero(Tile location, MainState mainState, Stats stats, Inv inv)
	{
		super(location, mainState, stats, inv);
	}

	@Override
	public XEntity copy(Tile copyLocation)
	{
		XHero copy = new XHero(copyLocation, mainState, stats, inv.copy());
		copy.stats.autoEquip(copy);
		return copy;
	}

	@Override
	public Image getImage()
	{
		return IMAGE_S;
	}

	@Override
	public boolean isEnemy(XEntity other)
	{
		return other instanceof XEnemy;
	}

	public Optional<ItemList> tryBuildingCosts(CostBlueprint cost, CommitType commitType)
	{
		if(inv.tryProvide(cost.costs, false, CommitType.LEAVE).isEmpty())
			return Optional.empty();
		return inv.tryProvide(cost.refundable, false, commitType);
	}

	public void buildBuilding(CostBlueprint costs, ItemList refundable, BuildingBlueprint blueprint)
	{
		if(blueprint.type == 0)
		{
			ProductionBuilding building = new ProductionBuilding(location, costs, refundable, blueprint);
			mainState.levelMap.addBuilding(building);
			building.claimFloor(mainState.levelMap);
		}
		else
		{
			mainState.levelMap.addBuilding(new Transporter(location, costs, refundable, blueprint));
		}
	}

	public void startTurn()
	{
		canMove = true;
		ap = 2;
		mainAction = true;
		revertLocation = location;
		canRevert = true;
	}

	public NState defaultState(boolean withMove)
	{
		if(withMove && canMove)
			return new CharacterMovementState(this);
		else if(mainAction)
			return new AttackTargetState(this);
		else
			return new CharacterInvState(this);
	}

	public boolean canMove()
	{
		return canMove;
	}

	public int getAp()
	{
		return ap;
	}

	public boolean isReady()
	{
		return mainAction;
	}

	public Tile getRevertLocation()
	{
		return revertLocation;
	}

	public boolean canRevert()
	{
		return !canMove && canRevert;
	}

	public void setMoved()
	{
		canMove = false;
	}

	public void takeAp(int take)
	{
		ap -= take;
	}

	public void mainActionTaken()
	{
		mainAction = false;
	}

	public void irreversible()
	{
		if(!canMove)
			canRevert = false;
	}

	public void revertMovement()
	{
		mainState.levelMap.moveEntity(this, revertLocation);
		canMove = true;
	}

	@Override
	public int classSave()
	{
		return 1;
	}
}