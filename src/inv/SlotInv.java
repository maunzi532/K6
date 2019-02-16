package inv;

import java.util.*;
import java.util.stream.*;

public class SlotInv implements Inv
{
	private final List<InvSlot> slots;

	public SlotInv(ItemList limits)
	{
		slots = limits.items.stream().map(InvSlot::new).collect(Collectors.toList());
	}

	@Override
	public void commit()
	{
		slots.forEach(InvSlot::commit);
	}

	@Override
	public void rollback()
	{
		slots.forEach(InvSlot::rollback);
	}

	@Override
	public List<Item> providedItemTypes()
	{
		return slots.stream().filter(e -> e.doesStackExist() && e.canProvide()).map(InvSlot::getStackItem).collect(Collectors.toList());
	}

	@Override
	public List<ItemView> viewItems(boolean withEmpty)
	{
		Stream<InvSlot> stream = withEmpty ? slots.stream() : slots.stream().filter(e -> e.getCurrentX() > 0);
		return stream.map(e -> new ItemView(e.getStackItem(), e.getCurrent(), e.getCurrentX(), e.getLimit()))
				.collect(Collectors.toList());
	}

	@Override
	public ItemView viewItem(Item item)
	{
		return slots.stream().filter(e -> item.canContain(e.getStackItem())).findFirst()
				.map(e -> new ItemView(e.getStackItem(), e.getCurrent(), e.getCurrentX(), e.getLimit()))
				.orElse(new ItemView(item, 0, 0));
	}

	@Override
	public int maxDecrease(ItemStack items)
	{
		return slots.stream().mapToInt(e -> e.canProvideX(items.item) ? e.maxDecrease() : 0).max().orElse(0);
	}

	@Override
	public int maxIncrease(ItemStack items)
	{
		return slots.stream().mapToInt(e -> e.fitsTypeX(items.item) ? e.maxIncrease(items) : 0).max().orElse(0);
	}

	@Override
	public ItemStack decrease(ItemStack items)
	{
		return slots.stream().filter(e -> e.canProvideX(items.item) && e.maxDecrease() >= items.count).findFirst().orElseThrow().decrease(items.count);
	}

	@Override
	public void increase(ItemStack items)
	{
		slots.stream().filter(e -> e.fitsTypeX(items.item) && e.maxIncrease(items) >= items.count).findFirst().orElseThrow().increase(items);
	}
}