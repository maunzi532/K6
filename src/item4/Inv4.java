package item4;

import java.util.*;

public interface Inv4
{
	List<NumberedStack4> viewItems();

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