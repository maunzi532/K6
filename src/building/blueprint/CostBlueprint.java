package building.blueprint;

import file.*;
import inv.*;
import java.util.*;
import java.util.stream.*;

public class CostBlueprint
{
	public final ItemList refundable;
	public final ItemList costs;
	public final List<RequiresFloorTiles> requiredFloorTiles;

	public CostBlueprint(ItemList refundable, ItemList costs,
			RequiresFloorTiles... requiredFloorTiles)
	{
		this.refundable = refundable;
		this.costs = costs;
		this.requiredFloorTiles = Arrays.asList(requiredFloorTiles);
	}

	public CostBlueprint(ItemList refundable, RequiresFloorTiles... requiredFloorTiles)
	{
		this.refundable = refundable;
		costs = new ItemList();
		this.requiredFloorTiles = Arrays.asList(requiredFloorTiles);
	}

	public CostBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		refundable = new ItemList(node.get(0));
		costs = new ItemList(node.get(1));
		requiredFloorTiles = node.get(2).inside.stream().map(RequiresFloorTiles::new).collect(Collectors.toList());
	}
}