package item4;

public record ItemStack4(Item4 item, int count)
{
	public boolean canAddAll(Item4 addItem, int addCount)
	{
		return item.equals(addItem) && addCount <= item.stackLimit() - count;
	}

	public int maxAdd(ItemStack4 add)
	{
		return maxAdd(add.item(), add.count());
	}

	public int maxAdd(Item4 addItem, int addCount)
	{
		if(item.equals(addItem))
			return Math.min(addCount, item.stackLimit() - count);
		else
			return 0;
	}
}