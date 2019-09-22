package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
import item.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class CostBlueprint
{
	public final ItemList refundable;
	public final ItemList costs;
	public final ItemList required;
	public final List<RequiresFloorTiles> requiredFloorTiles;

	public CostBlueprint(ItemList refundable, ItemList costs,
			RequiresFloorTiles... requiredFloorTiles)
	{
		this.refundable = refundable;
		this.costs = costs;
		this.requiredFloorTiles = Arrays.asList(requiredFloorTiles);
		required = costs.add(refundable);
	}

	public CostBlueprint(ItemList refundable, RequiresFloorTiles... requiredFloorTiles)
	{
		this.refundable = refundable;
		costs = new ItemList();
		this.requiredFloorTiles = Arrays.asList(requiredFloorTiles);
		required = costs.add(refundable);
	}

	public CostBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		refundable = new ItemList(node.get(0));
		costs = new ItemList(node.get(1));
		requiredFloorTiles = node.get(2).inside.stream().map(RequiresFloorTiles::new).collect(Collectors.toList());
		required = costs.add(refundable);
	}

	public CostBlueprint(JrsObject data)
	{
		refundable = new ItemList((JrsArray) data.get("Refundable"));
		costs = new ItemList((JrsArray) data.get("Costs"));
		required = costs.add(refundable);
		requiredFloorTiles = new ArrayList<>();
		((JrsArray) data.get("FloorTiles")).elements()
				.forEachRemaining(e -> requiredFloorTiles.add(new RequiresFloorTiles((JrsObject) e)));
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		var a2 = refundable.save(a1.startArrayField("Refundable")).end();
		a2 = costs.save(a2.startArrayField("Costs")).end();
		var a3 = a2.startArrayField("FloorTiles");
		for(RequiresFloorTiles ft1 : requiredFloorTiles)
		{
			a3 = ft1.save(a3.startObject()).end();
		}
		return a3.end();
	}
}