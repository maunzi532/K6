package inv;

import java.util.*;
import java.util.stream.*;

public class SlotInv implements Inv0, Inv
{
	private final List<InvSlot> slots;

	public SlotInv(ItemList limits)
	{
		slots = limits.items.stream().map(InvSlot::new).collect(Collectors.toList());
	}

	@Override
	public boolean ok()
	{
		return slots.stream().allMatch(InvSlot::ok);
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
	public List<Item> providedItemTypesX()
	{
		return slots.stream().filter(InvSlot::canProvideX).map(InvSlot::getStackItemC).collect(Collectors.toList());
	}

	@Override
	public List<ItemView> viewItems(boolean withEmpty)
	{
		Stream<InvSlot> stream = withEmpty ? slots.stream() : slots.stream().filter(e -> e.getCurrentX() > 0);
		return stream.map(e -> new ItemView(e.getStackItemC(), e.getCurrentC(), e.getCurrentX(), e.getLimit()))
				.collect(Collectors.toList());
	}

	@Override
	public ItemView viewRequiredItem(Item item)
	{
		return slots.stream().filter(e -> e.canProvideX() && item.canContain(e.getStackItemC())).findFirst()
				.map(e -> new ItemView(e.getStackItemC(), e.getCurrentC(), e.getCurrentX(), e.getLimit()))
				.orElse(new ItemView(item, 0, 0));
	}

	@Override
	public InvWeightView viewInvWeight()
	{
		return new InvWeightView(slots.stream().mapToInt(e -> e.getStackItemC().weight() * e.getCurrentC()).sum(),
				slots.stream().mapToInt(e -> e.getStackItemC().weight() * e.getCurrentX()).sum());
	}

	@Override
	public int maxDecrease(ItemStack items)
	{
		return slots.stream().mapToInt(e -> e.maxDecrease(items)).max().orElse(0);
	}

	@Override
	public int maxIncrease(ItemStack items)
	{
		return slots.stream().mapToInt(e -> e.maxIncrease(items)).max().orElse(0);
	}

	@Override
	public ItemStack decrease(ItemStack items)
	{
		return slots.stream().filter(e -> e.maxDecrease(items) >= items.count).findFirst().orElseThrow().decrease(items);
	}

	@Override
	public void increase(ItemStack items)
	{
		slots.stream().filter(e -> e.maxIncrease(items) >= items.count).findFirst().orElseThrow().increase(items);
	}

	@Override
	public boolean canGive(ItemStack itemStack, boolean unlimited)
	{
		return slots.stream().anyMatch(e -> e.canGive(itemStack, unlimited));
	}

	@Override
	public boolean give(ItemStack itemStack, boolean unlimited)
	{
		return slots.stream().anyMatch(e -> e.give(itemStack, unlimited));
	}

	@Override
	public Optional<ItemStack> wouldProvide(ItemStack itemStack, boolean unlimited)
	{
		return slots.stream().map(e -> e.wouldProvide(itemStack, unlimited)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
	}

	@Override
	public Optional<ItemStack> provide(ItemStack itemStack, boolean unlimited)
	{
		return slots.stream().map(e -> e.provide(itemStack, unlimited)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
	}

	@Override
	public boolean canAdd(ItemStack itemStack, boolean unlimited)
	{
		return slots.stream().anyMatch(e -> e.canAdd(itemStack, unlimited));
	}

	@Override
	public boolean add(ItemStack itemStack, boolean unlimited)
	{
		return slots.stream().anyMatch(e -> e.add(itemStack, unlimited));
	}
}