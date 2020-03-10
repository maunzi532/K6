package building.adv;

import building.blueprint.*;
import building.transport.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.util.*;
import levelMap.*;

public class XBuilding implements DoubleInv
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
		if(blueprint.productionBlueprint() != null)
		{
			function = new ProcessInv(blueprint.name(), blueprint.productionBlueprint());
		}
		else if(blueprint.transporterBlueprint() != null)
		{
			function = new Transport(blueprint.name(), blueprint.transporterBlueprint());
		}
		else
		{
			throw new RuntimeException();
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
	public String name()
	{
		return function.name();
	}

	@Override
	public Tile location()
	{
		return location;
	}

	@Override
	public boolean playerTradeable(boolean levelStarted)
	{
		return function.playerTradeable(levelStarted);
	}

	@Override
	public Inv inputInv()
	{
		return function.inputInv();
	}

	@Override
	public Inv outputInv()
	{
		return function.outputInv();
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
	public int inputPriority()
	{
		return function.inputPriority();
	}

	@Override
	public int outputPriority()
	{
		return function.outputPriority();
	}

	public void remove()
	{
		active = false;
	}

	public void productionPhase(LevelMap levelMap)
	{
		function.productionPhase(canWork(levelMap, false), levelMap, location);
	}

	public void transportPhase(LevelMap levelMap)
	{
		function.transportPhase(canWork(levelMap, false), levelMap);
	}

	public void afterProduction()
	{
		function.afterProduction();
	}

	public void afterTransport()
	{
		function.afterTransport();
	}

	public void loadConnect(LevelMap levelMap)
	{
		for(Tile tile : claimed)
		{
			levelMap.addOwner(tile, this);
		}
		function.loadConnect(levelMap, this);
	}

	public boolean canWork(LevelMap levelMap, boolean unclaimed)
	{
		return costBlueprint.requiredFloorTiles().stream().noneMatch(rft -> levelMap.y1.range(location, rft.minRange(), rft.maxRange()).stream()
				.filter(e -> okTile(e, levelMap, rft, unclaimed)).count() < rft.amount());
	}

	public ItemList allRefundable()
	{
		if(function.inputInv() == function.outputInv())
		{
			return refundable.add(function.outputInv().allItems());
		}
		else
		{
			return refundable.add(function.inputInv().allItems()).add(function.outputInv().allItems());
		}
	}

	public void toggleTargetClaimed(Tile target, LevelMap levelMap)
	{
		XBuilding owner = levelMap.getOwner(target);
		if(owner == this)
		{
			levelMap.removeOwner(target);
			claimed.remove(target);
		}
		else if(owner == null)
		{
			levelMap.addOwner(target, this);
			claimed.add(target);
		}
	}

	public void autoClaimFloor(LevelMap levelMap)
	{
		for(RequiresFloorTiles rft : costBlueprint.requiredFloorTiles())
		{
			int count = 0;
			for(Tile t1 : levelMap.y1.range(location, rft.minRange(), rft.maxRange()))
			{
				if(okTile(t1, levelMap, rft, true))
				{
					levelMap.addOwner(t1, this);
					claimed.add(t1);
					count++;
					if(count >= rft.amount())
						break;
				}
			}
		}
	}

	private boolean okTile(Tile t1, LevelMap levelMap, RequiresFloorTiles rft, boolean unclaimed)
	{
		return levelMap.getFloor(t1) != null && levelMap.getFloor(t1).type == rft.floorTileType()
				&& ((unclaimed && levelMap.getOwner(t1) == null) || levelMap.getOwner(t1) == this);
	}

	public XBuilding(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		active = true;
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
		if(data.get("Recipes") != null)
		{
			function = new ProcessInv(data, itemLoader, y1);
		}
		else if(data.get("Amount") != null)
		{
			function = new Transport(data, itemLoader, y1);
		}
		else
		{
			throw new RuntimeException();
		}
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		var a2 = a1.put("sx", y1.sx(location))
				.put("sy", y1.sy(location));
		a2 = costBlueprint.save(a2.startObjectField("Costs"), itemLoader).end();
		a2 = refundable.save(a2.startArrayField("Refundable"), itemLoader).end();
		var a3 = a2.startArrayField("Claimed");
		for(Tile tile : claimed)
		{
			a3 = a3.startObject().put("sx", y1.sx(tile)).put("sy", y1.sy(tile)).end();
		}
		a2 = a3.end();
		return function.save(a2, itemLoader, y1);
	}
}