package entity;

import geom.f1.*;
import item.*;
import item.inv.*;
import item.inv.transport.*;
import logic.*;

public class InvEntity extends XEntity implements DoubleInv
{
	protected Inv inv;

	public InvEntity(Tile location, MainState mainState, Stats stats, int weightLimit, ItemList itemList)
	{
		super(location, mainState, stats);
		inv = new WeightInv(weightLimit);
		addItems(itemList);
	}

	@Override
	public String name()
	{
		return "InvEntity";
	}

	@Override
	public Inv inputInv()
	{
		return inv;
	}

	@Override
	public Inv outputInv()
	{
		return inv;
	}

	public void addItems(ItemList itemList)
	{
		inv.tryAdd(itemList, false, CommitType.COMMIT);
	}
}