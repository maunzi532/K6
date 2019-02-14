package inv;

public class InvSlot3
{
	private InvStack3 stack;
	private boolean stackExists;
	private Item type;
	private int weightLimit;

	public InvSlot3(ItemStack limits)
	{
		type = limits.item;
		weightLimit = limits.count;
	}

	public Item getType()
	{
		return stackExists ? stack.item : type;
	}

	public ItemStack getStack()
	{
		return stackExists ? new ItemStack(stack.item, stack.getCurrent()) : new ItemStack(type, 0);
	}

	public int getWeight()
	{
		return stackExists ? stack.getWeight() : 0;
	}

	public int getCurrent()
	{
		return stackExists ? stack.getCurrent() : 0;
	}

	public int getDecrease()
	{
		return stack != null ? stack.getDecrease() : 0;
	}

	public int getIncrease()
	{
		return stack != null ? stack.getIncrease() : 0;
	}

	public int getWeightX()
	{
		return stack != null ? stack.getWeightX() : 0;
	}

	public Item getTypeX()
	{
		return stack != null ? stack.item : type;
	}

	public boolean fitsTypeX(Item item)
	{
		return stack != null ? stack.item.equals(item) : type.canContain(item);
	}

	public void commit()
	{
		if(stack != null)
		{
			stack.commit();
			if(stack.removable())
				stack = null;
		}
		stackExists = stack != null;
	}

	public void rollback()
	{
		if(stack != null)
		{
			stack.rollback();
			if(stack.removable())
				stack = null;
		}
	}

	public int maxDecrease()
	{
		if(stack != null)
			return stack.maxDecrease();
		else
			return 0;
	}

	public int maxIncrease(ItemStack items)
	{
		if(stack != null)
		{
			if(stack.item.equals(items.item))
				return stack.maxIncrease(weightLimit / stack.item.weight());
			else
				return 0;
		}
		else
		{
			if(type.canContain(items.item))
				return weightLimit / items.item.weight();
			else
				return 0;
		}
	}

	public ItemStack decrease(int by)
	{
		if(stack != null)
		{
			stack.decrease(by);
			return new ItemStack(stack.item, by);
		}
		else
		{
			return new ItemStack(type, 0);
		}
	}

	public void increase(ItemStack items)
	{
		if(stack != null)
		{
			stack.increase(items.count);
		}
		else
		{
			stack = new InvStack3(items);
		}
	}
}