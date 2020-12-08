package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.tile.*;
import item.*;
import java.io.*;
import system.*;

public final class Storage4 implements InvHolder, XSaveableS
{
	private final StorageInv inv;

	public Storage4()
	{
		inv = new StorageInv();
	}

	public Storage4(StorageInv inv)
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

	public static Storage4 load(JrsObject data, SystemScheme systemScheme)
	{
		return new Storage4(StorageInv.load(data, systemScheme));
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		inv.save(a1, systemScheme);
	}
}