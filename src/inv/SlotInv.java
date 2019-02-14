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

	public InvSlot getSlot(Item type)
	{
		return slots.stream().filter(e -> e.getType().equals(type)).findFirst().orElseThrow();
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
	public int viewCount(boolean withEmpty)
	{
		return withEmpty ? slots.size() : (int) slots.stream().filter(e -> e.getCurrentX() > 0).count();
	}

	@Override
	public Optional<ItemView> viewItem(int num, boolean withEmpty)
	{
		Stream<InvSlot> stream = withEmpty ? slots.stream() : slots.stream().filter(e -> e.getCurrentX() > 0);
		return stream.skip(num).findFirst().map(e -> new ItemView(e.getStackItem(), e.getCurrent(), e.getCurrentX(), e.getLimit()));
	}

	@Override
	public List<ItemView> viewItems(int start, int amount, boolean withEmpty)
	{
		Stream<InvSlot> stream = withEmpty ? slots.stream() : slots.stream().filter(e -> e.getCurrentX() > 0);
		return stream.skip(start).limit(amount)
				.map(e -> new ItemView(e.getStackItem(), e.getCurrent(), e.getCurrentX(), e.getLimit()))
				.collect(Collectors.toList());
	}

	@Override
	public int maxDecrease(ItemStack items)
	{
		return slots.stream().mapToInt(e -> e.fitsTypeX(items.item) ? e.maxDecrease() : 0).max().orElse(0);
	}

	@Override
	public int maxIncrease(ItemStack items)
	{
		return slots.stream().mapToInt(e -> e.fitsTypeX(items.item) ? e.maxIncrease(items) : 0).max().orElse(0);
	}

	@Override
	public ItemStack decrease(ItemStack items)
	{
		return slots.stream().filter(e -> e.fitsTypeX(items.item) && e.maxDecrease() >= items.count).findFirst().orElseThrow().decrease(items.count);
	}

	@Override
	public void increase(ItemStack items)
	{
		slots.stream().filter(e -> e.fitsTypeX(items.item) && e.maxIncrease(items) >= items.count).findFirst().orElseThrow().increase(items);
	}
}