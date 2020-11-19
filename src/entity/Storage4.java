package entity;

import doubleinv.*;
import geom.tile.*;
import item4.*;

public final class Storage4 implements InvHolder
{
	private final StorageInv4 inv;

	public Storage4()
	{
		inv = new StorageInv4();
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

	@Override
	public Inv4 inv()
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