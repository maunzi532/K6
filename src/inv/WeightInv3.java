package inv;

import java.util.*;
import java.util.stream.Collectors;

public class WeightInv3 implements Inv3
{
	private final List<InvStack3> stacks;
	private int currentW;
	private int decreaseW;
	private int increaseW;
	private final int limitW;

	public WeightInv3(int limitW)
	{
		stacks = new ArrayList<>();
		this.limitW = limitW;
	}

	@Override
	public void commit()
	{
		stacks.forEach(InvStack3::commit);
		stacks.removeIf(InvStack3::removable);
		currentW += increaseW - decreaseW;
		decreaseW = 0;
		increaseW = 0;
	}

	@Override
	public void rollback()
	{
		stacks.forEach(InvStack3::rollback);
		stacks.removeIf(InvStack3::removable);
		decreaseW = 0;
		increaseW = 0;
	}

	@Override
	public List<Item> providedItemTypes()
	{
		return stacks.stream().filter(InvStack3::canProvide).map(e -> e.item).collect(Collectors.toList());
	}

	@Override
	public int maxDecrease(ItemStack items)
	{
		return stacks.stream().filter(e -> items.item.canContain(e.item)).mapToInt(InvStack3::maxDecrease).findFirst().orElse(0);
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
		InvStack3 invStack = stacks.stream().filter(e -> items.item.canContain(e.item)).findFirst().orElseThrow();
		invStack.decrease(items.count);
		return new ItemStack(invStack.item, items.count);
	}

	@Override
	public void increase(ItemStack items)
	{
		increaseW += items.weight();
		Optional<InvStack3> invStack = stacks.stream().filter(e -> e.item.equals(items.item)).findFirst();
		if(invStack.isPresent())
		{
			invStack.get().increase(items.count);
		}
		else
		{
			stacks.add(new InvStack3(items));
		}
	}
}