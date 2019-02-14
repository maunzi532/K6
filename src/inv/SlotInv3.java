package inv;

import java.util.*;
import java.util.stream.Collectors;

public class SlotInv3 implements Inv3
{
	private final List<InvSlot3> slots;

	public SlotInv3(ItemList limits)
	{
		slots = limits.items.stream().map(InvSlot3::new).collect(Collectors.toList());
	}

	public InvSlot3 getSlot(Item type)
	{
		return slots.stream().filter(e -> e.getType().equals(type)).findFirst().orElseThrow();
	}

	@Override
	public void commit()
	{
		slots.forEach(InvSlot3::commit);
	}

	@Override
	public void rollback()
	{
		slots.forEach(InvSlot3::rollback);
	}

	@Override
	public List<Item> providedItemTypes()
	{
		return slots.stream().filter(e -> e.doesStackExist() && e.canProvide()).map(InvSlot3::getStackItem).collect(Collectors.toList());
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