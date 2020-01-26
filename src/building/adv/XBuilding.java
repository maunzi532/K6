package building.adv;

import building.blueprint.*;
import building.transport.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
import levelMap.*;

public class XBuilding implements MBuilding, DoubleInv
{
	private final Tile location;
	private final CostBlueprint costBlueprint;
	private final ItemList refundable;
	private final BuildingFunction function;
	private boolean active;

	public XBuilding(Tile location, CostBlueprint costBlueprint, ItemList refundable, BuildingFunction function)
	{
		this.location = location;
		this.costBlueprint = costBlueprint;
		this.refundable = refundable;
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
		function.productionPhase(levelMap, costBlueprint, location);
	}

	@Override
	public void afterProduction()
	{
		function.afterProduction();
	}

	@Override
	public void transportPhase(LevelMap levelMap)
	{
		function.transportPhase(levelMap);
	}

	@Override
	public void afterTransport()
	{
		function.afterTransport();
	}

	@Override
	public void loadConnect(LevelMap levelMap)
	{
		function.loadConnect(levelMap, this);
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

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1)
			throws IOException
	{
		return null;
	}
}