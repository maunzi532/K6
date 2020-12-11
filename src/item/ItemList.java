package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import load.*;

public record ItemList(List<ItemStack> items) implements XSaveableI
{
	public ItemList()
	{
		this(List.of());
	}

	public static ItemList load(JrsObject data, AllItemsList allItemsList)
	{
		List<ItemStack> stacks = LoadHelper.asList(data.get("Items"), e -> ItemStack.load(e, allItemsList));
		return new ItemList(stacks);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, AllItemsList allItemsList) throws IOException
	{
		XSaveableI.saveList("Items", items, a1, allItemsList);
	}
}