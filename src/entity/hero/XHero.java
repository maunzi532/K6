package entity.hero;

import building.ProductionBuilding;
import building.blueprint.*;
import entity.XEntity;
import geom.hex.Hex;
import inv.*;
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

	public boolean canBuildBuilding(CostBlueprint cost, CommitType buildIt)
	{
		//check floor tiles
		return inv.tryProvide(cost.required, false, buildIt).isPresent();
	}

	public void buildBuilding(BuildingBlueprint blueprint, CostBlueprint cost)
	{
		mainState.levelMap.addBuilding(new ProductionBuilding(location, blueprint));
	}
}