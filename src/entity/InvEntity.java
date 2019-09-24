package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import building.transport.*;
import java.io.*;
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

	public InvEntity(Tile location, MainState mainState, Stats stats, Inv inv)
	{
		super(location, mainState, stats);
		this.inv = inv;
	}

	@Override
	public DoubleInvType type()
	{
		return DoubleInvType.ENTITY;
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

	@Override
	public void afterTrading()
	{
		stats.afterTrading(this);
	}

	public void addItems(ItemList itemList)
	{
		inv.tryAdd(itemList, false, CommitType.COMMIT);
	}

	@Override
	public XEntity copy(Tile copyLocation)
	{
		InvEntity copy = new InvEntity(copyLocation, mainState, stats, inv.copy());
		copy.stats.autoEquip(copy);
		return copy;
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save2(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		return inv.save(a1.startObjectField("Inventory"), itemLoader).end();
	}
}