package statsystem;

import doubleinv.*;
import entity.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import levelmap.*;

public final class Entity2Builder
{
	private final LevelMap levelMap;
	private Tile location;
	private Stats stats;
	private int weightLimit = 20;
	private ItemList itemList = new ItemList();

	public Entity2Builder(LevelMap levelMap)
	{
		this.levelMap = levelMap;
	}

	public Entity2Builder setLocation(Tile location)
	{
		this.location = location;
		return this;
	}

	public Entity2Builder setStats(Stats stats)
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
		itemList = itemList.add(ItemList.create(item));
		return this;
	}

	public Entity2Builder addItems(ItemList items)
	{
		itemList.add(items);
		return this;
	}

	public void create(CharacterTeam team)
	{
		Inv inv = new WeightInv(weightLimit);
		inv.tryAdd(itemList);
		XCharacter entity;
		if(team == CharacterTeam.HERO)
		{
			entity = new XCharacter(CharacterTeam.HERO, true, 0, location,
					null, null, null, null, false);
		}
		else
		{
			entity = new XCharacter(team, false, 0, location,
					null, null, null, null, false);
		}
		stats.autoEquip(entity);
		if(levelMap.getFloor(location) == null)
			levelMap.setFloorTile(location, new FloorTile(0, FloorTileType.TECH));
		levelMap.addEntity(entity);
	}
}