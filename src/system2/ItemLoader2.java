package system2;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import system2.content.*;

public class ItemLoader2 implements ItemLoader
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
			return AttackItems2.INSTANCE.items[((JrsNumber) data.get("AttackItemCode")).getValue().intValue()];
		}
		throw new RuntimeException();
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> saveItem(ObjectComposer<T> a1, Item item) throws IOException
	{
		if(item instanceof Items)
		{
			return a1.put("ItemCode", ((Items) item).ordinal());
		}
		if(item instanceof AttackItem2)
		{
			return a1.put("AttackItemCode", AttackItems2.INSTANCE.itemListA.indexOf(item));
		}
		throw new RuntimeException();
	}
}