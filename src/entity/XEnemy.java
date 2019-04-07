package entity;

import geom.f1.*;
import item.*;
import logic.*;

public class XEnemy extends InvEntity
{
	public XEnemy(Tile location, MainState mainState, Stats stats, int weightLimit, ItemList itemList)
	{
		super(location, mainState, stats, weightLimit, itemList);
	}

	@Override
	public String name()
	{
		return "XEnemy";
	}

	@Override
	public boolean isEnemy(XEntity other)
	{
		return other instanceof XHero;
	}
}