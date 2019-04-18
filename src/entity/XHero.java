package entity;

import building.*;
import building.blueprint.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.util.*;
import javafx.scene.image.*;
import logic.*;

public class XHero extends InvEntity
{
	private static final Image IMAGE_S = new Image("S.png");

	private boolean moved;
	private int ap;
	private boolean finished;

	public XHero(Tile location, MainState mainState, Stats stats, int weightLimit, ItemList itemList)
	{
		super(location, mainState, stats, weightLimit, itemList);
	}

	@Override
	public Image getImage()
	{
		return IMAGE_S;
	}

	@Override
	public String name()
	{
		return "XHero";
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
		moved = false;
		ap = 2;
		finished = false;
	}

	public boolean isMoved()
	{
		return moved;
	}

	public int getAp()
	{
		return ap;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setMoved()
	{
		moved = true;
	}

	public void takeAp(int take)
	{
		ap -= take;
	}

	public void setFinished()
	{
		finished = true;
	}
}