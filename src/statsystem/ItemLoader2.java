package statsystem;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import statsystem.content.*;

public final class ItemLoader2 implements ItemLoader
{
	@Override
	public Item loadItem(JrsObject data)
	{
		if(data.get("ItemCode") != null)
		{
			return Items.values()[((JrsNumber) data.get("ItemCode")).getValue().intValue()];
		}
		if(data.get("AttackItemCode") != null)
		{
			return AttackItems.INSTANCE.items[((JrsNumber) data.get("AttackItemCode")).getValue().intValue()];
		}
		throw new IllegalArgumentException("ItemLoader2 must have either \"ItemCode\" or \"AttackItemCode\" element");
	}

	@Override
	public <T extends ComposerBase> void saveItem(ObjectComposer<T> a1, Item item, boolean end) throws IOException
	{
		if(item instanceof Items item1)
		{
			a1.put("ItemCode", item1.ordinal());
		}
		else if(item instanceof AttackItem)
		{
			a1.put("AttackItemCode", AttackItems.INSTANCE.itemListA.indexOf(item));
		}
		else
		{
			throw new RuntimeException("Wrong item class:" + item.getClass());
		}
		if(end)
		{
			a1.end();
		}
	}
}