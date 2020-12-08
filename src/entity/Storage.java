package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.tile.*;
import item.*;
import java.io.*;
import system.*;

public final class Storage implements InvHolder, XSaveableS
{
	private final StorageInv inv;

	public Storage()
	{
		inv = new StorageInv();
	}

	public Storage(StorageInv inv)
	{
		this.inv = inv;
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
	public Inv inv()
	{
		return inv;
	}

	@Override
	public void afterTrading(){}

	public static Storage load(JrsObject data, WorldSettings worldSettings)
	{
		return new Storage(StorageInv.load(data, worldSettings));
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		inv.save(a1, worldSettings);
	}
}