package entity;

import doubleinv.*;
import geom.f1.*;
import item.inv.*;

public class Storage implements DoubleInv
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
}