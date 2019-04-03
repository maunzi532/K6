package entity.hero;

import building.*;
import building.blueprint.*;
import entity.*;
import entity.enemy.*;
import geom.hex.*;
import item.*;
import item.inv.*;
import item.inv.transport.*;
import java.util.*;
import javafx.scene.image.*;
import logic.*;

public class XHero extends XEntity implements DoubleInv
{
	private static final int BASE_WEIGHT_LIMIT = 20;
	private static final Image IMAGE_S = new Image("S.png");

	private MainState mainState;
	private Inv inv;

	public XHero(Hex location, MainState mainState)
	{
		super(location);
		this.mainState = mainState;
		inv = new WeightInv(BASE_WEIGHT_LIMIT);
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
	public Inv inputInv()
	{
		return inv;
	}

	@Override
	public Inv outputInv()
	{
		return inv;
	}

	@Override
	public boolean isEnemy(MEntity other)
	{
		return other instanceof XEnemy;
	}

	public void addItems(ItemList itemList)
	{
		inv.tryAdd(itemList, false, CommitType.COMMIT);
	}

	public Optional<ItemList> tryBuildingCosts(CostBlueprint cost)
	{
		//check floor tiles
		if(inv.tryProvide(cost.costs, false, CommitType.LEAVE).isEmpty())
			return Optional.empty();
		return inv.tryProvide(cost.refundable, false, CommitType.COMMIT);
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

	public void removeBuilding(Buildable building)
	{
		mainState.levelMap.removeBuilding(building);
	}
}