package system2;

import entity.*;
import geom.f1.*;
import item.*;
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

	private void autoEquip(InvEntity entity, Stats2 stats)
	{
		stats.setLastUsed(((AttackItem2) entity.outputInv().viewRecipeItem(stats.getItemFilter()).item).attackModes().get(0));
	}

	public void create(boolean player)
	{
		if(player)
		{
			XHero xHero = new XHero(location, mainState, stats, weightLimit, itemList);
			autoEquip(xHero, stats);
			mainState.levelMap.addEntity(xHero);
		}
		else
		{
			XEnemy xEnemy = new XEnemy(location, mainState, stats, weightLimit, itemList);
			autoEquip(xEnemy, stats);
			mainState.levelMap.addEntity(xEnemy);
		}
	}
}