package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public final class StorageInv implements Inv, XSaveableI
{
	private final Map<Item, Integer> items;

	public StorageInv(ItemList itemList)
	{
		items = new HashMap<>();
		for(ItemStack itemStack : itemList.items())
		{
			add(itemStack.item(), itemStack.count());
		}
	}

	private List<Map.Entry<Item, Integer>> sorted()
	{
		return items.entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey().num())).collect(Collectors.toList());
	}

	@Override
	public List<NumberedStack> viewItems()
	{
		List<Map.Entry<Item, Integer>> sorted = sorted();
		return IntStream.range(0, sorted.size())
				.mapToObj(i -> new NumberedStack(sorted.get(i).getKey(), sorted.get(i).getValue(), false, null, i))
				.collect(Collectors.toList());
	}

	@Override
	public ItemList asItemList()
	{
		return new ItemList(sorted().stream().map(e -> new ItemStack(e.getKey(), e.getValue())).collect(Collectors.toList()));
	}

	@Override
	public void clear()
	{
		items.clear();
	}

	@Override
	public boolean canAdd(Item item, int count)
	{
		return true;
	}

	@Override
	public void add(Item item, int count)
	{
		if(!item.ghost())
			items.put(item, items.getOrDefault(item, 0) + count);
	}

	@Override
	public Optional<ItemStack> canTake(int num, int count)
	{
		List<Map.Entry<Item, Integer>> sorted = sorted();
		if(num >= 0 && num < items.size() && items.get(sorted.get(num).getKey()) >= count)
			return Optional.of(new ItemStack(sorted.get(num).getKey(), count));
		else
			return Optional.empty();
	}

	@Override
	public ItemStack take(int num, int count)
	{
		Item key = sorted().get(num).getKey();
		int current = items.get(key);
		if(count >= current)
		{
			items.remove(key);
		}
		else
		{
			items.put(key, current - count);
		}
		return new ItemStack(key, count);
	}

	public static StorageInv load(JrsObject data, AllItemsList allItemsList)
	{
		return new StorageInv(ItemList.load(data, allItemsList));
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, AllItemsList allItemsList) throws IOException
	{
		asItemList().save(a1, allItemsList);
	}
}