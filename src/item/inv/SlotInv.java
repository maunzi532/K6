package item.inv;

import item.*;
import item.view.*;
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
	public ItemView viewRecipeItem(Item item)
	{
		return slots.stream().filter(e -> item.canContain(e.getStackItemC())).findFirst()
				.map(e -> new ItemView(e.getStackItemC(), e.getCurrentC(), e.getCurrentX(), e.getLimit()))
				.orElse(new ItemView(item, 0, 0));
	}

	@Override
	public ItemView viewRequiredItem(Item item)
	{
		ItemStack itemStack = new ItemStack(item, 1);
		return slots.stream().map(e -> e.wouldProvide(itemStack, true))
				.filter(Optional::isPresent).map(Optional::get).findFirst()
				.map(e -> new ItemView(e.item, e.count, e.count))
				.orElse(new ItemView(item, 0, 0));
	}

	@Override
	public InvNumView viewInvWeight()
	{
		return new InvNumView(slots.stream().mapToInt(e -> e.getStackItemC().weight() * e.getCurrentC()).sum(),
				slots.stream().mapToInt(e -> e.getStackItemC().weight() * e.getCurrentX()).sum());
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

	@Override
	public List<Integer> save()
	{
		List<Integer> ints = new ArrayList<>();
		ints.add(slots.size());
		for(InvSlot invSlot : slots)
		{
			ints.add(invSlot.getLimit());
			List<Integer> typeSave = invSlot.getType().save();
			ints.add(typeSave.size());
			ints.addAll(typeSave);
			if(invSlot.stackExists())
			{
				ints.add(invSlot.getCurrentC());
				List<Integer> itemSave = invSlot.getStackItemC().save();
				ints.add(itemSave.size());
				ints.addAll(itemSave);
			}
			else
			{
				ints.add(-1);
			}
		}
		return ints;
	}
}