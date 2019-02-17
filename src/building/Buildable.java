package building;

import building.blueprint.CostBlueprint;
import geom.hex.Hex;
import item.ItemList;

public abstract class Buildable implements Building
{
	private Hex location;
	private CostBlueprint costs;
	private ItemList refundable;

	public Buildable(Hex location, CostBlueprint costs, ItemList refundable)
	{
		this.location = location;
		this.costs = costs;
		this.refundable = refundable;
	}

	@Override
	public Hex location()
	{
		return location;
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