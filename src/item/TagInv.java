package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import load.*;

public class TagInv implements Inv, XSaveableI
{
	private final int maxStacks;
	private final List<TagStack> stacks;

	public TagInv(int maxStacks)
	{
		this.maxStacks = maxStacks;
		stacks = new ArrayList<>();
	}

	public TagInv(int maxStacks, List<TagStack> stacks)
	{
		this.maxStacks = maxStacks;
		this.stacks = stacks;
	}

	public List<Item> taggedItems(String tag)
	{
		return stacks.stream().filter(e -> Objects.equals(e.tag(), tag)).map(e -> e.items().item()).collect(Collectors.toList());
	}

	@Override
	public List<NumberedStack> viewItems()
	{
		return IntStream.range(0, stacks.size()).mapToObj(i -> NumberedStack.tagged(stacks.get(i), i)).collect(Collectors.toList());
	}

	@Override
	public ItemList asItemList()
	{
		return new ItemList(stacks.stream().map(TagStack::items).collect(Collectors.toList()));
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
		for(TagStack stack : stacks)
		{
			int maxAdd = stack.items().maxAdd(item, toAdd);
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
			TagStack stack = stacks.get(i);
			int maxAdd = stack.items().maxAdd(item, toAdd);
			if(maxAdd > 0)
			{
				stacks.set(i, new TagStack(new ItemStack(item, stack.items().count() + maxAdd), stack.tag()));
				toAdd -= maxAdd;
				if(toAdd <= 0)
					return;
			}
		}
		for(int i = stacks.size(); i < maxStacks; i++)
		{
			int maxAdd = Math.min(toAdd, item.stackLimit());
			stacks.add(new TagStack(new ItemStack(item, maxAdd)));
			toAdd -= maxAdd;
			if(toAdd <= 0)
				return;
		}
		throw new RuntimeException("TryAdd Error");
	}

	@Override
	public Optional<ItemStack> canTake(int num, int count)
	{
		if(num >= 0 && num < stacks.size() && stacks.get(num).items().count() >= count)
			return Optional.of(new ItemStack(stacks.get(num).items().item(), count));
		else
			return Optional.empty();
	}

	@Override
	public ItemStack take(int num, int count)
	{
		TagStack stack = stacks.get(num);
		int current = stack.items().count();
		if(count >= current)
		{
			stacks.remove(num);
		}
		else
		{
			stacks.set(num, new TagStack(stack.items().item(), current - count, stack.tag()));
		}
		return new ItemStack(stack.items().item(), count);
	}

	public static TagInv load(JrsObject data, AllItemsList allItemsList)
	{
		int maxStacks = LoadHelper.asInt(data.get("MaxStacks"));
		List<TagStack> stacks = LoadHelper.asList(data.get("Stacks"), e -> TagStack.load(e, allItemsList));
		return new TagInv(maxStacks, stacks);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, AllItemsList allItemsList) throws IOException
	{
		a1.put("MaxStacks", maxStacks);
		XSaveableI.saveList("Stacks", stacks, a1, allItemsList);
	}
}