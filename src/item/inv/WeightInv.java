package item.inv;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import item.*;
import item.view.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.Collectors;

public class WeightInv implements Inv
{
	private final List<InvStack> stacks;
	private int currentW;
	private int decreaseW;
	private int increaseW;
	private int limitW;

	public WeightInv(int weightLimit)
	{
		stacks = new ArrayList<>();
		limitW = weightLimit;
	}

	@Override
	public List<Item> providedItemTypesX()
	{
		return stacks.stream().filter(InvStack::canProvideX).map(e -> e.item).collect(Collectors.toList());
	}

	@Override
	public List<ItemView> viewItems(boolean withEmpty)
	{
		return stacks.stream().map(e -> new ItemView(e.item, e.getCountC(), e.getCountX())).collect(Collectors.toList());
	}

	@Override
	public ItemView viewRecipeItem(Item item)
	{
		return stacks.stream().filter(e -> item.canContain(e.item)).findFirst()
				.map(e -> new ItemView(e.item, e.getCountC(), e.getCountX())).orElse(new ItemView(item, 0, 0));
	}

	@Override
	public ItemView viewRequiredItem(Item item)
	{
		ItemStack itemStack = new ItemStack(item, 1);
		return stacks.stream().map(e -> e.wouldProvide(itemStack, true))
				.filter(Optional::isPresent).map(Optional::get).findFirst()
				.map(e -> new ItemView(e.item, e.count, e.count))
				.orElse(new ItemView(item, 0, 0));
	}

	@Override
	public InvNumView viewInvWeight()
	{
		return new InvNumView(currentW, currentW + increaseW - decreaseW, limitW);
	}

	@Override
	public Inv copy()
	{
		WeightInv copy = new WeightInv(limitW);
		for(InvStack stack : stacks)
		{
			copy.stacks.add(new InvStack(stack));
		}
		copy.currentW = currentW;
		return copy;
	}

	@Override
	public boolean ok()
	{
		return currentW + increaseW <= limitW && stacks.stream().allMatch(InvStack::ok);
	}

	@Override
	public void commit()
	{
		stacks.forEach(InvStack::commit);
		stacks.removeIf(InvStack::removable);
		currentW += increaseW - decreaseW;
		decreaseW = 0;
		increaseW = 0;
	}

	@Override
	public void rollback()
	{
		stacks.forEach(InvStack::rollback);
		stacks.removeIf(InvStack::removable);
		decreaseW = 0;
		increaseW = 0;
	}

	@Override
	public boolean canGive(ItemStack itemStack, boolean unlimited)
	{
		return stacks.stream().anyMatch(e -> e.canGive(itemStack, unlimited));
	}

	@Override
	public boolean give(ItemStack itemStack, boolean unlimited)
	{
		if(stacks.stream().anyMatch(e -> e.give(itemStack, unlimited)))
		{
			decreaseW += itemStack.weight();
			return true;
		}
		return false;
	}

	@Override
	public Optional<ItemStack> wouldProvide(ItemStack itemStack, boolean unlimited)
	{
		return stacks.stream().map(e -> e.wouldProvide(itemStack, unlimited)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
	}

	@Override
	public Optional<ItemStack> provide(ItemStack itemStack, boolean unlimited)
	{
		Optional<ItemStack> provided = stacks.stream().map(e -> e.provide(itemStack, unlimited)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
		if(provided.isPresent())
		{
			decreaseW += itemStack.weight();
		}
		return provided;
	}

	@Override
	public boolean canAdd(ItemStack itemStack, boolean unlimited)
	{
		return unlimited || currentW + increaseW + itemStack.weight() <= limitW;
	}

	@Override
	public boolean add(ItemStack itemStack, boolean unlimited)
	{
		if(canAdd(itemStack, unlimited))
		{
			if(stacks.stream().noneMatch(e -> e.add(itemStack, unlimited)))
			{
				stacks.add(new InvStack(itemStack));
			}
			increaseW += itemStack.weight();
			return true;
		}
		return false;
	}

	@Override
	public List<Integer> save()
	{
		List<Integer> ints = new ArrayList<>();
		ints.add(currentW);
		ints.add(limitW);
		ints.add(stacks.size());
		for(InvStack invStack : stacks)
		{
			ints.add(invStack.getCountC());
			ints.addAll(invStack.item.save());
		}
		return ints;
	}

	public WeightInv(IntBuffer intBuffer, CombatSystem s1)
	{
		currentW = intBuffer.get();
		limitW = intBuffer.get();
		stacks = new ArrayList<>();
		int stackCount = intBuffer.get();
		for(int i = 0; i < stackCount; i++)
		{
			int itemCount = intBuffer.get();
			stacks.add(new InvStack(s1.loadItem(intBuffer), itemCount));
		}
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		var a2 = a1.put("WCurrent", currentW)
				.put("WLimit", limitW)
				.startArrayField("Stacks");
		for(InvStack invStack : stacks)
		{
			a2 = invStack.item.save(a2.startObject().put("Count", invStack.getCountC()).startObjectField("Item")).end().end();
		}
		return a2.end();
	}

	public WeightInv(JrsObject data, CombatSystem s1)
	{
		currentW = ((JrsNumber) data.get("WCurrent")).getValue().intValue();
		limitW = ((JrsNumber) data.get("WLimit")).getValue().intValue();
		stacks = new ArrayList<>();
		var array1 = (JrsArray) data.get("Stacks");
		array1.elements().forEachRemaining(object1 ->
		{
			int itemCount = ((JrsNumber) ((JrsObject) object1).get("Count")).getValue().intValue();
			stacks.add(new InvStack(s1.loadItem(((JrsObject) ((JrsObject) object1).get("Item"))), itemCount));
		});
	}
}