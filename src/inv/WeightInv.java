package inv;

import java.util.*;
import java.util.stream.Collectors;

public class WeightInv implements Inv
{
	private final List<InvStack> stacks;
	private int currentW;
	private int decreaseW;
	private int increaseW;
	private final int limitW;

	public WeightInv(int limitW)
	{
		stacks = new ArrayList<>();
		this.limitW = limitW;
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
	public List<Item> providedItemTypes()
	{
		return stacks.stream().filter(InvStack::canProvide).map(e -> e.item).collect(Collectors.toList());
	}

	@Override
	public int viewCount(boolean withEmpty)
	{
		return stacks.size();
	}

	@Override
	public Optional<ItemView> viewItem(int num, boolean withEmpty)
	{
		return stacks.stream().skip(num).findFirst().map(e -> new ItemView(e.item, e.getCurrent(), e.getCurrentX()));
	}

	@Override
	public List<ItemView> viewItems(int start, int amount, boolean withEmpty)
	{
		return stacks.stream().skip(start).limit(amount)
				.map(e -> new ItemView(e.item, e.getCurrent(), e.getCurrentX())).collect(Collectors.toList());
	}

	@Override
	public int maxDecrease(ItemStack items)
	{
		return stacks.stream().filter(e -> items.item.canContain(e.item)).mapToInt(InvStack::maxDecrease).findFirst().orElse(0);
	}

	@Override
	public int maxIncrease(ItemStack items)
	{
		return (limitW - (currentW + increaseW)) / items.item.weight();
	}

	@Override
	public ItemStack decrease(ItemStack items)
	{
		decreaseW += items.weight();
		InvStack invStack = stacks.stream().filter(e -> items.item.canContain(e.item)).findFirst().orElseThrow();
		invStack.decrease(items.count);
		return new ItemStack(invStack.item, items.count);
	}

	@Override
	public void increase(ItemStack items)
	{
		increaseW += items.weight();
		Optional<InvStack> invStack = stacks.stream().filter(e -> e.item.equals(items.item)).findFirst();
		if(invStack.isPresent())
		{
			invStack.get().increase(items.count);
		}
		else
		{
			stacks.add(new InvStack(items));
		}
	}
}