package building;

import building.blueprint.*;
import geom.f1.*;
import item.*;
import levelMap.*;

public abstract class Buildable implements MBuilding
{
	private boolean active;
	private Tile location;
	private CostBlueprint costs;
	private ItemList refundable;
	private String name;

	public Buildable(Tile location, CostBlueprint costs, ItemList refundable, String name)
	{
		active = true;
		this.location = location;
		this.costs = costs;
		this.refundable = refundable;
		this.name = name;
	}

	@Override
	public boolean active()
	{
		return active;
	}

	@Override
	public void remove()
	{
		active = false;
	}

	@Override
	public Tile location()
	{
		return location;
	}

	public String name()
	{
		return name;
	}

	public CostBlueprint getCosts()
	{
		return costs;
	}

	public ItemList getRefundable()
	{
		return refundable;
	}
}