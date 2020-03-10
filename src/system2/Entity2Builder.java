package system2;

import doubleinv.*;
import entity.*;
import geom.f1.*;
import item.*;
import levelMap.*;

public class Entity2Builder
{
	private final LevelMap levelMap;
	private final CombatSystem combatSystem;
	private Tile location;
	private Stats2 stats;
	private int weightLimit = 20;
	private ItemList itemList = new ItemList();

	public Entity2Builder(LevelMap levelMap, CombatSystem combatSystem)
	{
		this.levelMap = levelMap;
		this.combatSystem = combatSystem;
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
		XEntity entity;
		if(player)
		{
			entity = new XHero(location, combatSystem, stats, false, false, weightLimit, itemList);
		}
		else
		{
			entity = new XEnemy(location, combatSystem, stats, combatSystem.standardAI(), weightLimit, itemList);
		}
		stats.autoEquip(entity);
		if(levelMap.getFloor(location) == null)
			levelMap.setFloorTile(location, new FloorTile(0, FloorTileType.TECH));
		levelMap.addEntity(entity);
	}
}