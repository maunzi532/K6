package item4;

import java.util.*;
import java.util.stream.*;

public class LockableInv4 implements Inv4
{
	private final int maxStacks;
	private final List<LockStack4> stacks;
	public boolean keep;

	public LockableInv4(int maxStacks)
	{
		this.maxStacks = maxStacks;
		stacks = new ArrayList<>();
		keep = false;
	}

	@Override
	public List<NumberedStack4> viewItems()
	{
		return IntStream.range(0, stacks.size()).mapToObj(i -> NumberedStack4.tagged(stacks.get(i),
				stacks.get(i).locked() != null && keep, i)).collect(Collectors.toList());
	}

	@Override
	public boolean tryAdd(Item4 addItem, int addCount)
	{
		if(canAddAll(addItem, addCount))
		{
			int toAdd = addCount;
			for(int i = 0; i < stacks.size(); i++)
			{
				LockStack4 stack = stacks.get(i);
				if(stack.locked() == null)
				{
					int maxAdd = stack.items().maxAdd(addItem, toAdd);
					if(maxAdd > 0)
					{
						stacks.set(i, new LockStack4(new ItemStack4(addItem, stack.items().count() + maxAdd)));
						toAdd -= maxAdd;
						if(toAdd <= 0)
							return true;
					}
				}
			}
			for(int i = stacks.size(); i < maxStacks; i++)
			{
				int maxAdd = Math.min(toAdd, addItem.stackLimit());
				stacks.add(new LockStack4(new ItemStack4(addItem, maxAdd)));
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
		for(LockStack4 stack : stacks)
		{
			if(stack.locked() == null)
			{
				int maxAdd = stack.items().maxAdd(addItem, toAdd);
				toAdd -= maxAdd;
				if(toAdd <= 0)
					return true;
			}
		}
		return toAdd <= (maxStacks - stacks.size()) * addItem.stackLimit();
	}

	public List<Item4> lockedItems(String tag)
	{
		return stacks.stream().filter(e -> Objects.equals(e.locked(), tag)).map(e -> e.items().item()).collect(Collectors.toList());
	}
}