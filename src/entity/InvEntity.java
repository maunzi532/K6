package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import item.inv.transport.*;
import java.io.*;
import java.util.*;
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

	@Override
	public XEntity copy(Tile copyLocation)
	{
		InvEntity copy = new InvEntity(copyLocation, mainState, stats, inv.copy());
		copy.stats.autoEquip(copy);
		return copy;
	}

	@Override
	public List<Integer> save2()
	{
		return inv.save();
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save2(ObjectComposer<T> a1) throws IOException
	{
		return inv.save(a1.startObjectField("Inventory")).end();
	}
}