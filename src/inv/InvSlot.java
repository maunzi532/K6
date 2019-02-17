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

	@Override
	public boolean ok()
	{
		return stack == null || (stack.ok() && stack.getIncreasedX() <= limit);
	}

	@Override
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

	@Override
	public void rollback()
	{
		if(stack != null)
		{
			stack.rollback();
			if(stack.removable())
				stack = null;
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