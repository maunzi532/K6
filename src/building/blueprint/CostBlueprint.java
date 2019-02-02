package building.blueprint;

import inv.*;

public class CostBlueprint
{
	public final ItemList costs;
	public final ItemList refunds;

	public CostBlueprint(ItemList costs, ItemList refunds)
	{
		this.costs = costs;
		this.refunds = refunds;
	}

	public CostBlueprint(ItemList costs)
	{
		this.costs = costs;
		refunds = new ItemList();
	}
}