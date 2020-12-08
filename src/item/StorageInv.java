package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import system.*;

public class StorageInv implements Inv, XSaveableS
{
	private Map<Item, Integer> items;

	public StorageInv()
	{
		items = new HashMap<>();
	}

	public StorageInv(ItemList itemList)
	{
		items = new HashMap<>();
		itemList.items().forEach(e -> items.put(e.item(), e.count()));
	}

	@Override
	public List<NumberedStack> viewItems()
	{
		List<Map.Entry<Item, Integer>> list = new ArrayList<>(items.entrySet());
		return IntStream.range(0, list.size()).mapToObj(i ->
				NumberedStack.unlimited(list.get(i).getKey(), list.get(i).getValue(), false, i)).collect(Collectors.toList());
	}

	@Override
	public boolean canAddAll(Item addItem, int addCount)
	{
		return true;
	}

	@Override
	public boolean tryAdd(Item addItem, int addCount)
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

	@Override
	public ItemStack takeableNum(int num, int count)
	{
		Map.Entry<Item, Integer> entry = new ArrayList<>(items.entrySet()).get(num);
		return new ItemStack(entry.getKey(), Math.min(count, entry.getValue()));
	}

	@Override
	public ItemStack takeNum(int num, int count)
	{
		Map.Entry<Item, Integer> entry = new ArrayList<>(items.entrySet()).get(num);
		Item item = entry.getKey();
		int current = entry.getValue();
		if(count >= current)
		{
			items.remove(item);
			return new ItemStack(item, current);
		}
		else
		{
			items.put(item, current - count);
			return new ItemStack(item, count);
		}
	}

	private ItemList viewAsItemList()
	{
		return new ItemList(items.entrySet().stream()
				.map(e -> new ItemStack(e.getKey(), e.getValue())).collect(Collectors.toList()));
	}

	public static StorageInv load(JrsObject data, SystemScheme systemScheme)
	{
		return new StorageInv(ItemList.load(data, systemScheme));
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		viewAsItemList().save(a1, systemScheme);
	}
}