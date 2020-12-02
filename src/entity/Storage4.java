package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.tile.*;
import item4.*;
import java.io.*;
import system4.*;

public final class Storage4 implements InvHolder, XSaveableS
{
	private final StorageInv4 inv;

	public Storage4()
	{
		inv = new StorageInv4();
	}

	public Storage4(StorageInv4 inv)
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
	public Inv4 inv()
	{
		return inv;
	}

	@Override
	public void afterTrading(){}

	public static Storage4 load(JrsObject data, SystemScheme systemScheme)
	{
		return new Storage4(StorageInv4.load(data, systemScheme));
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		inv.save(a1, systemScheme);
	}
}