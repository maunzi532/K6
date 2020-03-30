package entity;

import doubleinv.*;
import geom.tile.*;
import item.inv.*;

public final class Storage implements DoubleInv
{
	private final WeightInv inv;

	public Storage()
	{
		inv = new WeightInv(-1);
	}

	@Override
	public DoubleInvType type()
	{
		return null;
	}

	@Override
	public String name()
	{
		return "Storage";
	}

	@Override
	public Tile location()
	{
		return null;
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
	public boolean active()
	{
		return true;
	}

	@Override
	public void afterTrading(){}
}