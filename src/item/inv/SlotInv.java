package item.inv;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import item.view.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class SlotInv implements Inv
{
	private final List<InvSlot> slots;

	public SlotInv(ItemList limits)
	{
		slots = limits.items.stream().map(InvSlot::new).collect(Collectors.toList());
	}

	private SlotInv(List<InvSlot> limits)
	{
		slots = limits.stream().map(InvSlot::new).collect(Collectors.toList());
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
	public InvNumView viewInvWeight()
	{
		return new InvNumView(slots.stream().mapToInt(e -> e.getStackItemC().weight() * e.getCurrentC()).sum(),
				slots.stream().mapToInt(e -> e.getStackItemC().weight() * e.getCurrentX()).sum());
	}

	@Override
	public Inv copy()
	{
		return new SlotInv(slots);
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

	public SlotInv(JrsObject data, ItemLoader itemLoader)
	{
		slots = new ArrayList<>();
		((JrsArray) data.get("Slots")).elements().forEachRemaining(object1 -> slots.add(new InvSlot((JrsObject) object1, itemLoader)));
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		var a2 = a1.startArrayField("Slots");
		for(InvSlot invSlot : slots)
		{
			a2 = invSlot.save(a2.startObject(), itemLoader).end();
		}
		return a2.end();
	}
}