package item.inv;

import item.*;
import item.view.*;
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
}