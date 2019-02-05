package building.blueprint;

import inv.*;
import java.util.*;

public class CostBlueprint
{
	public final ItemList costs;
	public final ItemList refunds;
	public final List<RequiresFloorTiles> requiredFloorTiles;

	public CostBlueprint(ItemList costs, ItemList refunds,
			RequiresFloorTiles... requiredFloorTiles)
	{
		this.costs = costs;
		this.refunds = refunds;
		this.requiredFloorTiles = Arrays.asList(requiredFloorTiles);
	}

	public CostBlueprint(ItemList costs, RequiresFloorTiles... requiredFloorTiles)
	{
		this.costs = costs;
		refunds = new ItemList();
		this.requiredFloorTiles = Arrays.asList(requiredFloorTiles);
	}
}