package building.adv;

import building.blueprint.*;
import building.transport.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.util.*;
import levelMap.*;

public class XBuilding implements MBuilding, DoubleInv
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

	@Override
	public void remove()
	{
		active = false;
	}

	@Override
	public void productionPhase(LevelMap levelMap)
	{
		function.productionPhase(canWork(levelMap, false), levelMap, location);
	}

	@Override
	public void transportPhase(LevelMap levelMap)
	{
		function.transportPhase(canWork(levelMap, false), levelMap);
	}

	@Override
	public void afterProduction()
	{
		function.afterProduction();
	}

	@Override
	public void afterTransport()
	{
		function.afterTransport();
	}

	@Override
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
		return costBlueprint.requiredFloorTiles.stream().noneMatch(rft -> levelMap.y1.range(location, rft.minRange, rft.maxRange).stream()
				.filter(e -> okTile(e, levelMap, rft, unclaimed)).count() < rft.amount);
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
		MBuilding owner = levelMap.getOwner(target);
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
		for(RequiresFloorTiles rft : costBlueprint.requiredFloorTiles)
		{
			int count = 0;
			for(Tile t1 : levelMap.y1.range(location, rft.minRange, rft.maxRange))
			{
				if(okTile(t1, levelMap, rft, true))
				{
					levelMap.addOwner(t1, this);
					claimed.add(t1);
					count++;
					if(count >= rft.amount)
						break;
				}
			}
		}
	}

	private boolean okTile(Tile t1, LevelMap levelMap, RequiresFloorTiles rft, boolean unclaimed)
	{
		return levelMap.getFloor(t1) != null && levelMap.getFloor(t1).type == rft.floorTileType
				&& ((unclaimed && levelMap.getOwner(t1) == null) || levelMap.getOwner(t1) == this);
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1)
			throws IOException
	{
		return null;
	}
}