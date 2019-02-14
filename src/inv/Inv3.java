package inv;

import java.util.stream.Collectors;

public interface Inv3
{
	void commit();

	void rollback();

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

	//unsafe
	default boolean canDecrease(ItemList itemList)
	{
		return itemList.items.stream().allMatch(this::canDecrease);
	}

	default boolean canIncrease(ItemList itemList)
	{
		return itemList.items.stream().allMatch(this::canIncrease);
	}

	default ItemList decrease(ItemList itemList)
	{
		return new ItemList(itemList.items.stream().map(this::decrease).collect(Collectors.toList()));
	}

	default void increase(ItemList itemList)
	{
		itemList.items.forEach(this::increase);
	}
}