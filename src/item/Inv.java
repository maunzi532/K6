package item;

import java.util.*;

public interface Inv
{
	List<NumberedStack> viewItems();

	ItemList asItemList();

	void clear();

	boolean canAdd(Item item, int count);

	void add(Item item, int count);

	Optional<ItemStack> canTake(int num, int count);

	ItemStack take(int num, int count);
}