package item;

import java.util.*;

public interface Inv
{
	List<NumberedStack> viewItems();

	default boolean canAddAll(ItemStack add)
	{
		return canAddAll(add.item(), add.count());
	}

	boolean canAddAll(Item addItem, int addCount);

	default boolean tryAdd(ItemStack add)
	{
		return tryAdd(add.item(), add.count());
	}

	boolean tryAdd(Item addItem, int addCount);

	ItemStack takeableNum(int num, int count);

	ItemStack takeNum(int num, int count);
}