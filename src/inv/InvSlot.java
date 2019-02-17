package inv;

import java.util.Optional;

public class InvSlot implements Inv0
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

	public boolean ok()
	{
		return stack == null || (stack.ok() && stack.getCountX() <= limit);
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

	public ItemStack decrease(ItemStack items)
	{
		if(stack != null)
		{
			stack.decrease(items.count);
			return new ItemStack(stack.item, items.count);
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

	@Override
	public boolean canGive(ItemStack itemStack, boolean unlimited)
	{
		return stack != null && stack.canGive(itemStack, unlimited);
	}

	@Override
	public boolean give(ItemStack itemStack, boolean unlimited)
	{
		return stack != null && stack.give(itemStack, unlimited);
	}

	@Override
	public Optional<ItemStack> wouldProvide(ItemStack itemStack, boolean unlimited)
	{
		return stack != null ? stack.wouldProvide(itemStack, unlimited) : Optional.empty();
	}

	@Override
	public Optional<ItemStack> provide(ItemStack itemStack, boolean unlimited)
	{
		return stack != null ? stack.provide(itemStack, unlimited) : Optional.empty();
	}

	@Override
	public boolean canAdd(ItemStack itemStack, boolean unlimited)
	{
		if(stack != null)
		{
			return stack.canAdd(itemStack, unlimited) && (unlimited || stack.getIncreasedX() + itemStack.count <= limit);
		}
		else
		{
			return type.canContain(itemStack.item) && (unlimited || itemStack.count <= limit);
		}
	}

	@Override
	public boolean add(ItemStack itemStack, boolean unlimited)
	{
		if(canAdd(itemStack, unlimited))
		{
			if(stack != null)
			{
				stack.add(itemStack, unlimited);
			}
			else
			{
				stack = new InvStack(itemStack);
			}
			return true;
		}
		return false;
	}
}