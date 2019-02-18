package building;

import building.blueprint.CostBlueprint;
import geom.hex.Hex;
import item.ItemList;

public abstract class Buildable implements Building
{
	private Hex location;
	private CostBlueprint costs;
	private ItemList refundable;
	private String name;

	public Buildable(Hex location, CostBlueprint costs, ItemList refundable, String name)
	{
		this.location = location;
		this.costs = costs;
		this.refundable = refundable;
		this.name = name;
	}

	@Override
	public Hex location()
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