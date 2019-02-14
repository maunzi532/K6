package entity.hero;

import entity.*;
import hex.*;
import inv.*;

public class XHero extends XEntity implements DoubleInv
{
	private Inv inv;

	public XHero(Hex location)
	{
		super(location);
		inv = new WeightInv(20);
	}

	@Override
	public String name()
	{
		return "XHero";
	}

	@Override
	public Hex location()
	{
		return location;
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
	public int inputPriority()
	{
		return -1;
	}

	@Override
	public int outputPriority()
	{
		return -1;
	}

	public void addItems(ItemList itemList)
	{
		inv.tryIncrease(itemList);
		inv.commit();
	}
}