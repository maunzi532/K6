package item4;

public interface Inv4
{
	default boolean canAddAll(ItemStack4 add)
	{
		return canAddAll(add.item(), add.count());
	}

	boolean canAddAll(Item4 addItem, int addCount);

	default boolean tryAdd(ItemStack4 add)
	{
		return tryAdd(add.item(), add.count());
	}

	boolean tryAdd(Item4 addItem, int addCount);
}