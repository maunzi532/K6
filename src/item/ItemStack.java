package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;

public class ItemStack
{
	public final Item item;
	public final int count;

	public ItemStack(Item item, int count)
	{
		this.item = item;
		this.count = count;
	}

	public int weight()
	{
		return item.weight() * count;
	}

	public ItemStack(JrsObject data, ItemLoader itemLoader)
	{
		item = itemLoader.loadItem(data);
		if(data.get("Amount") != null)
		{
			count = ((JrsNumber) data.get("Amount")).getValue().intValue();
		}
		else
		{
			count = 1;
		}
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		itemLoader.saveItem(a1, item, false);
		if(count != 1)
		{
			a1.put("Amount", count);
		}
		a1.end();
	}
}