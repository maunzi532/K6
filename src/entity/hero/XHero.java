package entity.hero;

import building.*;
import building.blueprint.*;
import entity.XEntity;
import geom.hex.Hex;
import item.*;
import item.inv.*;
import item.inv.transport.DoubleInv;
import java.util.Optional;
import logic.MainState;

public class XHero extends XEntity implements DoubleInv
{
	private MainState mainState;
	private Inv inv;

	public XHero(Hex location, MainState mainState)
	{
		super(location);
		this.mainState = mainState;
		inv = new WeightInv(20);
	}

	@Override
	public String name()
	{
		return "XHero";
	}

	@Override
	public Hex location()
	{
		return location;
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
	public int inputPriority()
	{
		return -1;
	}

	@Override
	public int outputPriority()
	{
		return -1;
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
		mainState.levelMap.addBuilding(new ProductionBuilding(location, costs, refundable, blueprint));
	}

	public void removeBuilding(Buildable building)
	{
		mainState.levelMap.removeBuilding(building);
	}
}