package item4;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import load.*;
import system4.*;

public record ItemList4(List<ItemStack4> items) implements XSaveableS
{
	public ItemList4()
	{
		this(List.of());
	}

	public static ItemList4 load(JrsObject data, SystemScheme systemScheme)
	{
		List<ItemStack4> stacks = LoadHelper.asList(data.get("Items"), e -> ItemStack4.load(e, systemScheme));
		return new ItemList4(stacks);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		XSaveableS.saveList("Items", items, a1, systemScheme);
	}
}