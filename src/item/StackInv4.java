package item;

import java.util.*;
import java.util.stream.*;

public class StackInv4 implements Inv4
{
	private final int maxStacks;
	private final List<ItemStack4> stacks;

	public StackInv4(int maxStacks)
	{
		this.maxStacks = maxStacks;
		stacks = new ArrayList<>();
	}

	@Override
	public List<NumberedStack4> viewItems()
	{
		return IntStream.range(0, stacks.size()).mapToObj(i -> NumberedStack4.create(stacks.get(i), i)).collect(Collectors.toList());
	}

	@Override
	public boolean tryAdd(Item4 addItem, int addCount)
	{
		if(canAddAll(addItem, addCount))
		{
			int toAdd = addCount;
			for(int i = 0; i < stacks.size(); i++)
			{
				int maxAdd = stacks.get(i).maxAdd(addItem, toAdd);
				if(maxAdd > 0)
				{
					stacks.set(i, new ItemStack4(addItem, stacks.get(i).count() + maxAdd));
					toAdd -= maxAdd;
					if(toAdd <= 0)
						return true;
				}
			}
			for(int i = stacks.size(); i < maxStacks; i++)
			{
				int maxAdd = Math.min(toAdd, addItem.stackLimit());
				stacks.add(new ItemStack4(addItem, maxAdd));
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
	public boolean canAddAll(Item4 addItem, int addCount)
	{
		int toAdd = addCount;
		for(ItemStack4 stack : stacks)
		{
			int maxAdd = stack.maxAdd(addItem, toAdd);
			toAdd -= maxAdd;
			if(toAdd <= 0)
				return true;
		}
		return toAdd <= (maxStacks - stacks.size()) * addItem.stackLimit();
	}

	@Override
	public ItemStack4 takeableNum(int num, int count)
	{
		ItemStack4 stack = stacks.get(num);
		return new ItemStack4(stack.item(), Math.min(count, stack.count()));
	}

	@Override
	public ItemStack4 takeNum(int num, int count)
	{
		ItemStack4 stack = stacks.get(num);
		Item4 item = stack.item();
		int current = stack.count();
		if(count >= current)
		{
			stacks.remove(num);
			return new ItemStack4(item, current);
		}
		else
		{
			stacks.set(num, new ItemStack4(item, current - count));
			return new ItemStack4(item, count);
		}
	}
}