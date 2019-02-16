package inv;

public class InvSlot
{
	private InvStack stack;
	private boolean stackExists;
	private Item type;
	private int limit;

	public InvSlot(ItemStack limits)
	{
		type = limits.item;
		limit = limits.count;
	}

	public Item getStackItemC()
	{
		return stackExists ? stack.item : type;
	}

	public boolean canProvideX()
	{
		return stack != null && stack.canProvideX();
	}

	public int getCurrentX()
	{
		return stack != null ? stack.getCountX() : 0;
	}

	public int getCurrentC()
	{
		return stackExists ? stack.getCountC() : 0;
	}

	public int getLimit()
	{
		return limit;
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

	public int maxDecrease(ItemStack items)
	{
		if(stack != null && stack.getCountX() > 0 && items.item.canContain(stack.item))
			return stack.maxDecrease();
		else
			return 0;
	}

	public int maxIncrease(ItemStack items)
	{
		if(stack != null)
		{
			if(stack.item.equals(items.item))
				return stack.maxIncrease(limit);
			else
				return 0;
		}
		else
		{
			if(type.canContain(items.item))
				return limit;
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
			stack = new InvStack(items);
		}
	}
}