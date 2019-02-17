package item;

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

	public ItemList add(ItemList other)
	{
		return new ItemList(Stream.concat(items.stream(), other.items.stream())
				.collect(Collectors.groupingBy((ItemStack e) -> e.item, Collectors.summingInt((ItemStack f) -> f.count)))
				.entrySet().stream().map(e -> new ItemStack(e.getKey(), e.getValue())).collect(Collectors.toList()));
	}

	public ItemList(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		items = node.inside.stream().map(ItemStack::new).collect(Collectors.toList());
	}
}