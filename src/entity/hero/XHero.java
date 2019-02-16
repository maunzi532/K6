package entity.hero;

import building.ProductionBuilding;
import building.blueprint.*;
import entity.XEntity;
import geom.hex.Hex;
import inv.*;
import levelMap.LevelMap;

public class XHero extends XEntity implements DoubleInv
{
	private LevelMap levelMap;
	private Inv inv;

	public XHero(Hex location, LevelMap levelMap)
	{
		super(location);
		this.levelMap = levelMap;
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
		inv.tryIncrease(itemList);
		inv.commit();
	}

	public boolean canBuildBuilding(CostBlueprint cost)
	{
		//check floor tiles
		return inv.tryDecrease(cost.required).isPresent();
	}

	public void rollbackInv()
	{
		inv.rollback();;
	}

	public void buildBuilding(BuildingBlueprint blueprint, CostBlueprint cost)
	{
		inv.commit();
		levelMap.addBuilding(new ProductionBuilding(location, blueprint));
	}
}