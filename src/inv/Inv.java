package inv;

import java.util.*;

public interface Inv
{
	void commit();

	void rollback();

	List<Item> providedItemTypesX();

	List<ItemView> viewItems(boolean withEmpty);

	ItemView viewRequiredItem(Item item);

	InvWeightView viewInvWeight();

	int maxDecrease(ItemStack items);

	int maxIncrease(ItemStack items);

	ItemStack decrease(ItemStack items);

	void increase(ItemStack items);

	default boolean canDecrease(ItemStack items)
	{
		return maxDecrease(items) >= items.count;
	}

	default boolean canIncrease(ItemStack items)
	{
		return maxIncrease(items) >= items.count;
	}

	default Optional<ItemList> tryDecrease(ItemList itemList)
	{
		List<ItemStack> stacks = new ArrayList<>();
		for(ItemStack items : itemList.items)
		{
			if(canDecrease(items))
			{
				stacks.add(decrease(items));
			}
			else
			{
				rollback();
				return Optional.empty();
			}
		}
		return Optional.of(new ItemList(stacks));
	}

	default boolean tryIncrease(ItemList itemList)
	{
		for(ItemStack items : itemList.items)
		{
			if(canIncrease(items))
			{
				increase(items);
			}
			else
			{
				rollback();
				return false;
			}
		}
		return true;
	}
}