package item;

import file.*;

public class ItemStack
{
	public final Item item;
	public final int count;

	public ItemStack(Item item, int count)
	{
		this.item = item;
		this.count = count;
	}

	public ItemStack(BlueprintNode node)
	{
		if(node.size() == 0)
		{
			item = Items.valueOf(node.data);
			count = 1;
		}
		else
		{
			item = Items.valueOf(node.data);
			count = node.get(0).dataInt();
		}
	}

	public int weight()
	{
		return item.weight() * count;
	}

	public ItemStack merge(ItemStack other)
	{
		return new ItemStack(item, count + other.count);
	}
}