package item;

import java.util.*;
import java.util.stream.*;

public class StackInv implements Inv
{
	private final int maxStacks;
	private final List<ItemStack> stacks;

	public StackInv(int maxStacks)
	{
		this.maxStacks = maxStacks;
		stacks = new ArrayList<>();
	}

	@Override
	public List<NumberedStack> viewItems()
	{
		return IntStream.range(0, stacks.size()).mapToObj(i -> NumberedStack.create(stacks.get(i), i)).collect(Collectors.toList());
	}

	@Override
	public ItemList asItemList()
	{
		return new ItemList(stacks);
	}

	@Override
	public void clear()
	{
		stacks.clear();
	}

	@Override
	public boolean canAdd(Item item, int count)
	{
		int toAdd = count;
		for(ItemStack stack : stacks)
		{
			int maxAdd = stack.maxAdd(item, toAdd);
			toAdd -= maxAdd;
			if(toAdd <= 0)
				return true;
		}
		return toAdd <= (maxStacks - stacks.size()) * item.stackLimit();
	}

	@Override
	public void add(Item item, int count)
	{
		int toAdd = count;
		for(int i = 0; i < stacks.size(); i++)
		{
			ItemStack stack = stacks.get(i);
			int maxAdd = stack.maxAdd(item, toAdd);
			if(maxAdd > 0)
			{
				stacks.set(i, new ItemStack(item, stack.count() + maxAdd));
				toAdd -= maxAdd;
				if(toAdd <= 0)
					return;
			}
		}
		for(int i = stacks.size(); i < maxStacks; i++)
		{
			int maxAdd = Math.min(toAdd, item.stackLimit());
			stacks.add(new ItemStack(item, maxAdd));
			toAdd -= maxAdd;
			if(toAdd <= 0)
				return;
		}
		throw new RuntimeException("TryAdd Error");
	}

	@Override
	public Optional<ItemStack> canTake(int num, int count)
	{
		if(num >= 0 && num < stacks.size() && stacks.get(num).count() >= count)
			return Optional.of(new ItemStack(stacks.get(num).item(), count));
		else
			return Optional.empty();
	}

	@Override
	public ItemStack take(int num, int count)
	{
		ItemStack stack = stacks.get(num);
		int current = stack.count();
		if(count >= current)
		{
			stacks.remove(num);
		}
		else
		{
			stacks.set(num, new ItemStack(stack.item(), current - count));
		}
		return new ItemStack(stack.item(), count);
	}
}