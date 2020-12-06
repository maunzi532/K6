package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import load.*;
import system.*;

public class TagInv4 implements Inv4, XSaveableS
{
	private final int maxStacks;
	private final List<TagStack4> stacks;
	public boolean keep;

	public TagInv4(int maxStacks)
	{
		this.maxStacks = maxStacks;
		stacks = new ArrayList<>();
		keep = false;
	}

	public TagInv4(int maxStacks, List<TagStack4> stacks)
	{
		this.maxStacks = maxStacks;
		this.stacks = stacks;
		keep = false;
	}

	@Override
	public List<NumberedStack4> viewItems()
	{
		return IntStream.range(0, stacks.size()).mapToObj(i -> NumberedStack4.tagged(stacks.get(i),
				stacks.get(i).tag() != null && keep, i)).collect(Collectors.toList());
	}

	@Override
	public boolean tryAdd(Item4 addItem, int addCount)
	{
		if(canAddAll(addItem, addCount))
		{
			int toAdd = addCount;
			for(int i = 0; i < stacks.size(); i++)
			{
				TagStack4 stack = stacks.get(i);
				if(stack.tag() == null)
				{
					int maxAdd = stack.items().maxAdd(addItem, toAdd);
					if(maxAdd > 0)
					{
						stacks.set(i, new TagStack4(new ItemStack4(addItem, stack.items().count() + maxAdd)));
						toAdd -= maxAdd;
						if(toAdd <= 0)
							return true;
					}
				}
			}
			for(int i = stacks.size(); i < maxStacks; i++)
			{
				int maxAdd = Math.min(toAdd, addItem.stackLimit());
				stacks.add(new TagStack4(new ItemStack4(addItem, maxAdd)));
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
		for(TagStack4 stack : stacks)
		{
			if(stack.tag() == null)
			{
				int maxAdd = stack.items().maxAdd(addItem, toAdd);
				toAdd -= maxAdd;
				if(toAdd <= 0)
					return true;
			}
		}
		return toAdd <= (maxStacks - stacks.size()) * addItem.stackLimit();
	}

	public List<Item4> taggedItems(String tag)
	{
		return stacks.stream().filter(e -> Objects.equals(e.tag(), tag)).map(e -> e.items().item()).collect(Collectors.toList());
	}

	@Override
	public ItemStack4 takeableNum(int num, int count)
	{
		TagStack4 stack = stacks.get(num);
		return new ItemStack4(stack.items().item(), Math.min(count, stack.items().count()));
	}

	@Override
	public ItemStack4 takeNum(int num, int count)
	{
		TagStack4 stack = stacks.get(num);
		Item4 item = stack.items().item();
		int current = stack.items().count();
		if(count >= current)
		{
			stacks.remove(num);
			return new ItemStack4(item, current);
		}
		else
		{
			stacks.set(num, new TagStack4(item, current - count, stack.tag()));
			return new ItemStack4(item, count);
		}
	}

	public static TagInv4 load(JrsObject data, SystemScheme systemScheme)
	{
		int maxStacks = LoadHelper.asInt(data.get("MaxStacks"));
		List<TagStack4> stacks = LoadHelper.asList(data.get("Stacks"), e -> TagStack4.load(e, systemScheme));
		return new TagInv4(maxStacks, stacks);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		a1.put("MaxStacks", maxStacks);
		XSaveableS.saveList("Stacks", stacks, a1, systemScheme);
	}
}