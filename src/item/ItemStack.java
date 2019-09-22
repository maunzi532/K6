package item;

public class ItemStack
{
	public final Item item;
	public final int count;

	public ItemStack(Item item, int count)
	{
		this.item = item;
		this.count = count;
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