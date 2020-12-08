package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import load.*;
import system.*;

public record ItemList(List<ItemStack> items) implements XSaveableS
{
	public ItemList()
	{
		this(List.of());
	}

	public static ItemList load(JrsObject data, WorldSettings worldSettings)
	{
		List<ItemStack> stacks = LoadHelper.asList(data.get("Items"), e -> ItemStack.load(e, worldSettings));
		return new ItemList(stacks);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		XSaveableS.saveList("Items", items, a1, worldSettings);
	}
}