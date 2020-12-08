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
	public boolean tryAdd(Item addItem, int addCount)
	{
		if(canAddAll(addItem, addCount))
		{
			int toAdd = addCount;
			for(int i = 0; i < stacks.size(); i++)
			{
				int maxAdd = stacks.get(i).maxAdd(addItem, toAdd);
				if(maxAdd > 0)
				{
					stacks.set(i, new ItemStack(addItem, stacks.get(i).count() + maxAdd));
					toAdd -= maxAdd;
					if(toAdd <= 0)
						return true;
				}
			}
			for(int i = stacks.size(); i < maxStacks; i++)
			{
				int maxAdd = Math.min(toAdd, addItem.stackLimit());
				stacks.add(new ItemStack(addItem, maxAdd));
				toAdd -= maxAdd;
				if(toAdd <= 0)
					return true;
			}
			throw new RuntimeException("TryAdd Error");
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean canAddAll(Item addItem, int addCount)
	{
		int toAdd = addCount;
		for(ItemStack stack : stacks)
		{
			int maxAdd = stack.maxAdd(addItem, toAdd);
			toAdd -= maxAdd;
			if(toAdd <= 0)
				return true;
		}
		return toAdd <= (maxStacks - stacks.size()) * addItem.stackLimit();
	}

	@Override
	public ItemStack takeableNum(int num, int count)
	{
		ItemStack stack = stacks.get(num);
		return new ItemStack(stack.item(), Math.min(count, stack.count()));
	}

	@Override
	public ItemStack takeNum(int num, int count)
	{
		ItemStack stack = stacks.get(num);
		Item item = stack.item();
		int current = stack.count();
		if(count >= current)
		{
			stacks.remove(num);
			return new ItemStack(item, current);
		}
		else
		{
			stacks.set(num, new ItemStack(item, current - count));
			return new ItemStack(item, count);
		}
	}
}