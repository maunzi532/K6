package system2;

import doubleinv.*;
import entity.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import levelMap.*;
import system2.analysis.*;

public class Entity2Builder
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
		itemList = itemList.add(new ItemList(item));
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
			entity = new XCharacter(team, 0, location, stats, inv,
					new NoAI(), new TurnResources(location), new StartingSettings(false, false));
		}
		else
		{
			entity = new XCharacter(team, 0, location, stats, inv,
					new StandardAI(levelMap), new TurnResources(location), null);
		}
		stats.autoEquip(entity);
		if(levelMap.getFloor(location) == null)
			levelMap.setFloorTile(location, new FloorTile(0, FloorTileType.TECH));
		levelMap.addEntity(entity);
	}
}