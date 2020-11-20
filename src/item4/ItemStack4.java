package item4;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import load.*;
import system4.*;

public record ItemStack4(Item4 item, int count) implements XSaveableS
{
	public boolean canAddAll(Item4 addItem, int addCount)
	{
		return item.equals(addItem) && addCount <= item.stackLimit() - count;
	}

	public int maxAdd(ItemStack4 add)
	{
		return maxAdd(add.item(), add.count());
	}

	public int maxAdd(Item4 addItem, int addCount)
	{
		if(item.equals(addItem))
			return Math.min(addCount, item.stackLimit() - count);
		else
			return 0;
	}

	public static ItemStack4 load(JrsObject data, SystemScheme systemScheme)
	{
		Item4 item = systemScheme.getItem(data.get("Item").asText());
		int count = LoadHelper.asInt(data.get("Count"));
		return new ItemStack4(item, count);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		a1.put("Item", systemScheme.saveItem(item));
		a1.put("Count", count);
	}
}