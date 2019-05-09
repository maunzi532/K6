package system2;

import entity.*;
import geom.f1.*;
import item.*;
import levelMap.*;
import logic.*;

public class Entity2Builder
{
	private final MainState mainState;
	private Tile location;
	private Stats2 stats;
	private int weightLimit = 20;
	private ItemList itemList = new ItemList();

	public Entity2Builder(MainState mainState)
	{
		this.mainState = mainState;
	}

	public Entity2Builder setLocation(Tile location)
	{
		this.location = location;
		return this;
	}

	public Entity2Builder setStats(Stats2 stats)
	{
		this.stats = stats;
		return this;
	}

	public Entity2Builder setWeightLimit(int weightLimit)
	{
		this.weightLimit = weightLimit;
		return this;
	}

	public Entity2Builder addItem(Item item)
	{
		itemList = itemList.add(new ItemList(item));
		return this;
	}

	public Entity2Builder addItems(ItemList items)
	{
		itemList.add(items);
		return this;
	}

	public void create(boolean player)
	{
		InvEntity entity;
		if(player)
		{
			entity = new XHero(location, mainState, stats, weightLimit, itemList);
		}
		else
		{
			entity = new XEnemy(location, mainState, stats, weightLimit, itemList);
		}
		stats.autoEquip(entity);
		if(mainState.levelMap.getFloor(location) == null)
			mainState.levelMap.setFloorTile(location, new FloorTile(0, FloorTileType.TECH));
		mainState.levelMap.addEntity(entity);
	}
}