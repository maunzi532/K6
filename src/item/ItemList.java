package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.items.*;
import java.io.*;
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

	public ItemList(JrsArray data)
	{
		items = new ArrayList<>();
		data.elements().forEachRemaining(e -> items.add(ls1(e)));
	}

	private ItemStack ls1(JrsValue data)
	{
		if(data.get("Amount") != null)
		{
			return new ItemStack(Items.values()[((JrsNumber) ((JrsObject) data).get("ItemCode")).getValue().intValue()],
					((JrsNumber) ((JrsObject) data).get("Amount")).getValue().intValue());
		}
		else
		{
			return new ItemStack(Items.values()[((JrsNumber) data.get("ItemCode")).getValue().intValue()], 1);
		}
	}

	public <T extends ComposerBase> ArrayComposer<T> save(ArrayComposer<T> a1) throws IOException
	{
		for(ItemStack stack : items)
		{
			if(stack.count == 1)
			{
				a1 = stack.item.save(a1.startObject()).end();
			}
			else
			{
				a1 = stack.item.save(a1.startObject()).put("Amount", stack.count).end();
			}
		}
		return a1;
	}
}