package building.adv;

import arrow.*;
import building.blueprint.*;
import building.transport.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.util.*;

public final class XBuilding implements DoubleInv
{
	private final Tile location;
	private final CostBlueprint costBlueprint;
	private final ItemList refundable;
	private final List<Tile> claimed;
	private final BuildingFunction function;
	private boolean active;

	public XBuilding(Tile location, CostBlueprint costBlueprint, ItemList refundable, BuildingFunction function)
	{
		this.location = location;
		this.costBlueprint = costBlueprint;
		this.refundable = refundable;
		claimed = new ArrayList<>();
		this.function = function;
		active = true;
	}

	public XBuilding(Tile location, CostBlueprint costBlueprint, ItemList refundable, BuildingBlueprint blueprint)
	{
		this.location = location;
		this.costBlueprint = costBlueprint;
		this.refundable = refundable;
		claimed = new ArrayList<>();
		if(blueprint.production() != null)
		{
			function = new ProcessInv(blueprint.name(), blueprint.production());
		}
		else if(blueprint.transporter() != null)
		{
			function = new Transport(blueprint.name(), blueprint.transporter());
		}
		else
		{
			throw new RuntimeException("Missing building function");
		}
		active = true;
	}

	public BuildingFunction function()
	{
		return function;
	}

	public CostBlueprint costBlueprint()
	{
		return costBlueprint;
	}

	public List<Tile> claimed()
	{
		return claimed;
	}

	@Override
	public DoubleInvType type()
	{
		return DoubleInvType.BUILDING;
	}

	@Override
	public CharSequence name()
	{
		return function.name();
	}

	@Override
	public Tile location()
	{
		return location;
	}

	@Override
	public Inv inv(TradeDirection tradeDirection)
	{
		return function.inv(tradeDirection);
	}

	@Override
	public boolean active()
	{
		return active;
	}

	@Override
	public void afterTrading()
	{
		function.afterTrading();
	}

	@Override
	public int priority(TradeDirection tradeDirection)
	{
		return function.priority(tradeDirection);
	}

	public void remove()
	{
		active = false;
	}

	public void addClaimed(Tile t1)
	{
		claimed.add(t1);
	}

	public void removeClaimed(Tile t1)
	{
		claimed.remove(t1);
	}

	public void productionPhase(Arrows arrows, boolean canWork)
	{
		function.productionPhase(canWork, arrows, location);
	}

	public void transportPhase(Arrows arrows, boolean canWork)
	{
		function.transportPhase(canWork, arrows);
	}

	public void afterProduction()
	{
		function.afterProduction();
	}

	public void afterTransport()
	{
		function.afterTransport();
	}

	public ItemList allRefundable()
	{
		Inv inputInv = function.inv(TradeDirection.TAKE);
		Inv outputInv = function.inv(TradeDirection.GIVE);
		if(inputInv == outputInv)
		{
			return refundable.add(outputInv.allItems());
		}
		else
		{
			return refundable.add(inputInv.allItems()).add(outputInv.allItems());
		}
	}

	public XBuilding(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		costBlueprint = CostBlueprint.create((JrsObject) data.get("Costs"), itemLoader);
		refundable = new ItemList((JrsArray) data.get("Refundable"), itemLoader);
		claimed = new ArrayList<>();
		if(data.get("Claimed") != null)
		{
			((JrsArray) data.get("Claimed")).elements().forEachRemaining(e ->
					claimed.add(y1.create2(((JrsNumber) ((JrsObject) e).get("sx")).getValue().intValue(),
							((JrsNumber) ((JrsObject) e).get("sy")).getValue().intValue())));
		}
		JrsObject data2 = (JrsObject) data.get("Function");
		if(data2.get("Recipes") != null)
		{
			function = new ProcessInv(data2, itemLoader);
		}
		else if(data2.get("Amount") != null)
		{
			function = new Transport(data2, itemLoader, y1);
		}
		else
		{
			throw new IllegalArgumentException("Missing building function");
		}
		active = true;
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1.put("sx", y1.sx(location));
		a1.put("sy", y1.sy(location));
		costBlueprint.save(a1.startObjectField("Costs"), itemLoader);
		refundable.save(a1.startArrayField("Refundable"), itemLoader);
		var a2 = a1.startArrayField("Claimed");
		for(Tile tile : claimed)
		{
			var a3 = a2.startObject();
			a3.put("sx", y1.sx(tile));
			a3.put("sy", y1.sy(tile));
			a3.end();
		}
		a2.end();
		function.save(a1.startObjectField("Function"), itemLoader, y1);
		a1.end();
	}
}