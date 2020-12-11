package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import load.*;

public record ItemStack(Item item, int count) implements XSaveableI
{
	public boolean canAddAll(Item addItem, int addCount)
	{
		return item.equals(addItem) && addCount <= item.stackLimit() - count;
	}

	public int maxAdd(ItemStack add)
	{
		return maxAdd(add.item(), add.count());
	}

	public int maxAdd(Item addItem, int addCount)
	{
		if(item.equals(addItem))
			return Math.min(addCount, item.stackLimit() - count);
		else
			return 0;
	}

	public static ItemStack load(JrsObject data, AllItemsList allItemsList)
	{
		Item item = allItemsList.getItem(data.get("Item").asText());
		int count = LoadHelper.asInt(data.get("Count"));
		return new ItemStack(item, count);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, AllItemsList allItemsList) throws IOException
	{
		a1.put("Item", allItemsList.saveItem(item));
		a1.put("Count", count);
	}
}