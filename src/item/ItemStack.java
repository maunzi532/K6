package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import load.*;
import system.*;

public record ItemStack(Item item, int count) implements XSaveableS
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

	public static ItemStack load(JrsObject data, WorldSettings worldSettings)
	{
		Item item = worldSettings.getItem(data.get("Item").asText());
		int count = LoadHelper.asInt(data.get("Count"));
		return new ItemStack(item, count);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		a1.put("Item", worldSettings.saveItem(item));
		a1.put("Count", count);
	}
}