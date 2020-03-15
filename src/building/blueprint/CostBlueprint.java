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

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		refundable.save(a1.startArrayField("Refundable"), itemLoader);
		costs.save(a1.startArrayField("Costs"), itemLoader);
		var a2 = a1.startArrayField("FloorTiles");
		for(RequiresFloorTiles ft1 : requiredFloorTiles)
		{
			ft1.save(a2.startObject());
		}
		a2.end();
		a1.end();
	}
}