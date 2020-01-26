package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public record CostBlueprint(ItemList refundable, ItemList costs, ItemList required, List<RequiresFloorTiles> requiredFloorTiles)
{
	public static CostBlueprint create(JrsObject data, ItemLoader itemLoader)
	{
		ItemList refundable = new ItemList((JrsArray) data.get("Refundable"), itemLoader);
		ItemList costs = new ItemList((JrsArray) data.get("Costs"), itemLoader);
		ItemList required = refundable.add(costs);
		List<RequiresFloorTiles> requiredFloorTiles = new ArrayList<>();
		((JrsArray) data.get("FloorTiles")).elements().forEachRemaining(e -> requiredFloorTiles.add(RequiresFloorTiles.create((JrsObject) e)));
		return new CostBlueprint(refundable, costs, required, requiredFloorTiles);
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