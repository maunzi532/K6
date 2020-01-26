package building;

import building.blueprint.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.*;
import java.io.*;
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

	@Override
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

	public Buildable(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		active = true;
		location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		costs = new CostBlueprint((JrsObject) data.get("Costs"), itemLoader);
		refundable = new ItemList((JrsArray) data.get("Refundable"), itemLoader);
		name = data.get("Name").asText();
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		var a2 = a1.put("sx", y1.sx(location))
				.put("sy", y1.sy(location));
		a2 = costs.save(a2.startObjectField("Costs"), itemLoader).end();
		a2 = refundable.save(a2.startArrayField("Refundable"), itemLoader).end();
		return a2.put("Name", name);
	}
}