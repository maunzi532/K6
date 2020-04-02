package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public final class ItemList
{
	public final List<ItemStack> items;

	public ItemList()
	{
		items = List.of();
	}

	public ItemList(List<ItemStack> items)
	{
		this.items = items;
	}

	public static ItemList create(Item... items)
	{
		return new ItemList(Arrays.stream(items).map(e -> new ItemStack(e, 1)).collect(Collectors.toList()));
	}

	public ItemList add(ItemList other)
	{
		return new ItemList(Stream.concat(items.stream(), other.items.stream())
				.collect(Collectors.groupingBy(stack -> stack.item, Collectors.summingInt(stack1 -> stack1.count)))
				.entrySet().stream().map(e -> new ItemStack(e.getKey(), e.getValue())).collect(Collectors.toList()));
	}

	public ItemList(JrsArray data, ItemLoader itemLoader)
	{
		items = new ArrayList<>();
		data.elements().forEachRemaining(e -> items.add(new ItemStack((JrsObject) e, itemLoader)));
	}

	public <T extends ComposerBase> void save(ArrayComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		for(ItemStack stack : items)
		{
			stack.save(a1.startObject(), itemLoader);
		}
		a1.end();
	}
}