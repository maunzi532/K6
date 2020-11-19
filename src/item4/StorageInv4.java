package item4;

import java.util.*;
import java.util.stream.*;

public class StorageInv4 implements Inv4
{
	private Map<Item4, Integer> items;

	public StorageInv4()
	{
		items = new HashMap<>();
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
}