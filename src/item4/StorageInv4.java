package item4;

import java.util.*;

public class StorageInv4 implements Inv4
{
	private Map<Item4, Integer> items;

	public StorageInv4()
	{
		items = new HashMap<>();
	}

	@Override
	public boolean canAddAll(Item4 addItem, int addCount)
	{
		return true;
	}

	@Override
	public boolean tryAdd(Item4 addItem, int addCount)
	{
		if(items.containsKey(addItem))
		{
			items.put(addItem, items.get(addItem) + addCount);
		}
		else
		{
			items.put(addItem, addCount);
		}
		return true;
	}
}