package item.inv;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public final class InvSlot implements Inv0
{
	private InvStack stack;
	private boolean stackExists;
	private final Item type;
	private final int limit;

	public InvSlot(ItemStack limits)
	{
		type = limits.item;
		limit = limits.count;
	}

	private InvSlot(InvStack stack, boolean stackExists, Item type, int limit)
	{
		this.stack = stack;
		this.stackExists = stackExists;
		this.type = type;
		this.limit = limit;
	}

	public InvSlot copy()
	{
		return new InvSlot(stack != null ? stack.copy() : null, stackExists, type, limit);
	}

	public Item getType()
	{
		return type;
	}

	public boolean stackExists()
	{
		return stackExists;
	}

	public Item getStackItemC()
	{
		return stackExists ? stack.item : type;
	}

	public boolean canProvideX()
	{
		return stack != null && stack.canChangedProvide();
	}

	public int getCurrentX()
	{
		return stack != null ? stack.changedCount() : 0;
	}

	public int getCurrentC()
	{
		return stackExists ? stack.currentCount() : 0;
	}

	public int getLimit()
	{
		return limit;
	}

	@Override
	public boolean ok()
	{
		return stack == null || (stack.ok() && stack.getChangedIncreased() <= limit);
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
	public Optional<ItemStack> showProvidable(ItemStack itemStack, boolean unlimited)
	{
		return stack != null ? stack.showProvidable(itemStack, unlimited) : Optional.empty();
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
			return stack.canAdd(itemStack, unlimited) && (unlimited || stack.getChangedIncreased() + itemStack.count <= limit);
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

	public InvSlot(JrsObject data, ItemLoader itemLoader)
	{
		ItemStack restrictions = new ItemStack((JrsObject) data.get("Restrictions"), itemLoader);
		type = restrictions.item;
		limit = restrictions.count;
		if(data.get("Inside") != null)
		{
			stackExists = true;
			stack = new InvStack(new ItemStack((JrsObject) data.get("Inside"), itemLoader));
			stack.commit();
		}
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		new ItemStack(type, limit).save(a1.startObjectField("Restrictions"), itemLoader);
		if(stackExists)
		{
			stack.toItemStack().save(a1.startObjectField("Inside"), itemLoader);
		}
		a1.end();
	}
}