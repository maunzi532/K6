package inv;

import java.util.*;

public class Inv2
{
	private final boolean specificLimits;
	private final HashMap<Item, InvStack> items;
	private int current = 0;
	private int decrease = 0;
	private int increase = 0;
	public final int limit;

	public Inv2()
	{
		specificLimits = false;
		items = new HashMap<>();
		limit = -1;
	}

	public Inv2(int limit)
	{
		specificLimits = false;
		items = new HashMap<>();
		this.limit = limit;
	}

	public Inv2(InvStack... initial)
	{
		specificLimits = true;
		items = new HashMap<>();
		Arrays.stream(initial).forEach(e -> items.put(e.item, e));
		limit = -1;
	}

	public Inv2(ItemList limits)
	{
		specificLimits = true;
		items = new HashMap<>();
		limits.items.forEach(e -> items.put(e.item, new InvStack(e)));
		limit = -1;
	}

	public boolean isSpecificLimits()
	{
		return specificLimits;
	}

	public Set<Item> getItemTypes()
	{
		return items.keySet();
	}

	public int getCurrent()
	{
		return current;
	}

	public int getDecrease()
	{
		return decrease;
	}

	public int getIncrease()
	{
		return increase;
	}

	public void commit()
	{
		current += increase - decrease;
		decrease = 0;
		increase = 0;
		items.forEach((e, f) -> f.commit());
		if(!specificLimits)
			items.entrySet().removeIf(e -> e.getValue().removable());
	}

	public void rollback()
	{
		decrease = 0;
		increase = 0;
		items.forEach((e, f) -> f.rollback());
		if(!specificLimits)
			items.entrySet().removeIf(e -> e.getValue().removable());
	}

	public boolean canDecrease(Item item, int by)
	{
		if(by <= 0)
			return false;
		InvStack stack = items.get(item);
		return stack != null && stack.canDecrease(by) && current - decrease - by >= 0;
	}

	public int maxDecrease(Item item)
	{
		InvStack stack = items.get(item);
		return stack != null ? stack.maxDecrease() : 0;
	}

	public boolean canDecrease(ItemList itemList)
	{
		return itemList.items.stream().allMatch(e -> maxDecrease(e.item) >= e.count);
	}

	public boolean canIncrease(Item item, int by)
	{
		if(by <= 0)
			return false;
		if(limit >= 0 && current + increase + by > limit)
			return false;
		if(!specificLimits)
			return true;
		InvStack stack = items.get(item);
		return stack != null && stack.canIncrease(by);
	}

	public int maxIncrease(Item item, int limited)
	{
		int limit1 = limit >= 0 ? limit - current - increase : limited;
		InvStack stack = items.get(item);
		if(stack != null)
		{
			return Math.min(limit1, stack.maxIncrease(limited));
		}
		else
		{
			return specificLimits ? 0 : limit1;
		}
	}

	public boolean canIncrease(ItemList itemList)
	{
		return itemList.items.stream().allMatch(e -> maxIncrease(e.item, e.count) >= e.count);
	}

	public void decrease(Item item, int by)
	{
		decrease += by;
		items.get(item).decrease(by);
	}

	public void decrease(ItemList itemList)
	{
		itemList.items.forEach(e -> decrease(e.item, e.count));
	}

	public void increase(Item item, int by)
	{
		increase += by;
		InvStack stack = items.get(item);
		if(stack == null)
		{
			stack = new InvStack(item);
			items.put(item, stack);
		}
		stack.increase(by);
	}

	public void increase(ItemList itemList)
	{
		itemList.items.forEach(e -> increase(e.item, e.count));
	}

	@Override
	public String toString()
	{
		return "Inv2{" +
				"specificLimits=" + specificLimits +
				", items=" + items +
				", current=" + current +
				", decrease=" + decrease +
				", increase=" + increase +
				", limit=" + limit +
				'}';
	}
}