package item4;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import system4.*;

public class StorageInv4 implements Inv4, XSaveableS
{
	private Map<Item4, Integer> items;

	public StorageInv4()
	{
		items = new HashMap<>();
	}

	public StorageInv4(ItemList4 itemList)
	{
		items = new HashMap<>();
		itemList.items().forEach(e -> items.put(e.item(), e.count()));
	}

	@Override
	public List<NumberedStack4> viewItems()
	{
		List<Map.Entry<Item4, Integer>> list = new ArrayList<>(items.entrySet());
		return IntStream.range(0, list.size()).mapToObj(i ->
				NumberedStack4.unlimited(list.get(i).getKey(), list.get(i).getValue(), false, i)).collect(Collectors.toList());
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

	@Override
	public ItemStack4 takeableNum(int num, int count)
	{
		Map.Entry<Item4, Integer> entry = new ArrayList<>(items.entrySet()).get(num);
		return new ItemStack4(entry.getKey(), Math.min(count, entry.getValue()));
	}

	@Override
	public ItemStack4 takeNum(int num, int count)
	{
		Map.Entry<Item4, Integer> entry = new ArrayList<>(items.entrySet()).get(num);
		Item4 item = entry.getKey();
		int current = entry.getValue();
		if(count >= current)
		{
			items.remove(item);
			return new ItemStack4(item, current);
		}
		else
		{
			items.put(item, current - count);
			return new ItemStack4(item, count);
		}
	}

	private ItemList4 viewAsItemList()
	{
		return new ItemList4(items.entrySet().stream()
				.map(e -> new ItemStack4(e.getKey(), e.getValue())).collect(Collectors.toList()));
	}

	public static StorageInv4 load(JrsObject data, SystemScheme systemScheme)
	{
		return new StorageInv4(ItemList4.load(data, systemScheme));
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		viewAsItemList().save(a1, systemScheme);
	}
}