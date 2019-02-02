package inv;

import java.util.*;
import java.util.stream.*;

public class ItemList
{
	public final List<ItemStack> items;

	public ItemList()
	{
		items = List.of();
	}

	public ItemList(ItemStack... items)
	{
		this.items = Arrays.asList(items);
	}

	public ItemList(List<ItemStack> items)
	{
		this.items = items;
	}

	public ItemList(Item... items)
	{
		this.items = Arrays.stream(items).map(e -> new ItemStack(e, 1)).collect(Collectors.toList());
	}

	/*public ItemList(int w, Item... items)
	{
		this.items = Arrays.stream(items).collect(Collectors.groupingBy(e -> e, Collectors.counting())).entrySet()
				.stream().map(e -> new ItemStack(e.getKey(), e.getValue().intValue())).collect(Collectors.toList());
	}*/
}