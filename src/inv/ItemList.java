package inv;

import file.*;
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

	public ItemList(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		items = node.inside.stream().map(ItemStack::new).collect(Collectors.toList());
	}
}