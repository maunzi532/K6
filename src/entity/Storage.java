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
	public CharSequence name()
	{
		return "storage.name";
	}

	@Override
	public Tile location()
	{
		return null;
	}

	public Inv inv()
	{
		return inv;
	}

	@Override
	public Inv inv(TradeDirection tradeDirection)
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