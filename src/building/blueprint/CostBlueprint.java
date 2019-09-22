package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public class CostBlueprint
{
	public final ItemList refundable;
	public final ItemList costs;
	public final ItemList required;
	public final List<RequiresFloorTiles> requiredFloorTiles;

	public CostBlueprint(ItemList refundable, ItemList costs,
			List<RequiresFloorTiles> requiredFloorTiles)
	{
		this.refundable = refundable;
		this.costs = costs;
		required = refundable.add(costs);
		this.requiredFloorTiles = requiredFloorTiles;
	}

	public CostBlueprint(JrsObject data, ItemLoader itemLoader)
	{
		refundable = new ItemList((JrsArray) data.get("Refundable"), itemLoader);
		costs = new ItemList((JrsArray) data.get("Costs"), itemLoader);
		required = refundable.add(costs);
		requiredFloorTiles = new ArrayList<>();
		((JrsArray) data.get("FloorTiles")).elements()
				.forEachRemaining(e -> requiredFloorTiles.add(new RequiresFloorTiles((JrsObject) e)));
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		var a2 = refundable.save(a1.startArrayField("Refundable"), itemLoader).end();
		a2 = costs.save(a2.startArrayField("Costs"), itemLoader).end();
		var a3 = a2.startArrayField("FloorTiles");
		for(RequiresFloorTiles ft1 : requiredFloorTiles)
		{
			a3 = ft1.save(a3.startObject()).end();
		}
		return a3.end();
	}
}