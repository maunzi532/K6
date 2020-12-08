package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import load.*;
import system.*;

public class TagInv implements Inv, XSaveableS
{
	private final int maxStacks;
	private final List<TagStack> stacks;
	public boolean keep;

	public TagInv(int maxStacks)
	{
		this.maxStacks = maxStacks;
		stacks = new ArrayList<>();
		keep = false;
	}

	public TagInv(int maxStacks, List<TagStack> stacks)
	{
		this.maxStacks = maxStacks;
		this.stacks = stacks;
		keep = false;
	}

	@Override
	public List<NumberedStack> viewItems()
	{
		return IntStream.range(0, stacks.size()).mapToObj(i -> NumberedStack.tagged(stacks.get(i),
				stacks.get(i).tag() != null && keep, i)).collect(Collectors.toList());
	}

	@Override
	public boolean tryAdd(Item addItem, int addCount)
	{
		if(canAddAll(addItem, addCount))
		{
			int toAdd = addCount;
			for(int i = 0; i < stacks.size(); i++)
			{
				TagStack stack = stacks.get(i);
				if(stack.tag() == null)
				{
					int maxAdd = stack.items().maxAdd(addItem, toAdd);
					if(maxAdd > 0)
					{
						stacks.set(i, new TagStack(new ItemStack(addItem, stack.items().count() + maxAdd)));
						toAdd -= maxAdd;
						if(toAdd <= 0)
							return true;
					}
				}
			}
			for(int i = stacks.size(); i < maxStacks; i++)
			{
				int maxAdd = Math.min(toAdd, addItem.stackLimit());
				stacks.add(new TagStack(new ItemStack(addItem, maxAdd)));
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
		for(TagStack stack : stacks)
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

	public List<Item> taggedItems(String tag)
	{
		return stacks.stream().filter(e -> Objects.equals(e.tag(), tag)).map(e -> e.items().item()).collect(Collectors.toList());
	}

	@Override
	public ItemStack takeableNum(int num, int count)
	{
		TagStack stack = stacks.get(num);
		return new ItemStack(stack.items().item(), Math.min(count, stack.items().count()));
	}

	@Override
	public ItemStack takeNum(int num, int count)
	{
		TagStack stack = stacks.get(num);
		Item item = stack.items().item();
		int current = stack.items().count();
		if(count >= current)
		{
			stacks.remove(num);
			return new ItemStack(item, current);
		}
		else
		{
			stacks.set(num, new TagStack(item, current - count, stack.tag()));
			return new ItemStack(item, count);
		}
	}

	public static TagInv load(JrsObject data, SystemScheme systemScheme)
	{
		int maxStacks = LoadHelper.asInt(data.get("MaxStacks"));
		List<TagStack> stacks = LoadHelper.asList(data.get("Stacks"), e -> TagStack.load(e, systemScheme));
		return new TagInv(maxStacks, stacks);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		a1.put("MaxStacks", maxStacks);
		XSaveableS.saveList("Stacks", stacks, a1, systemScheme);
	}
}