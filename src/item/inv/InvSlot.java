package item.inv;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public class InvSlot implements Inv0
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

	public InvSlot(InvSlot copy)
	{
		stackExists = copy.stackExists;
		if(stackExists)
			stack = new InvStack(copy.stack);
		type = copy.type;
		limit = copy.limit;
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

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		a1 = new ItemStack(type, limit).save(a1.startObjectField("Restrictions"), itemLoader).end();
		if(stackExists)
		{
			a1 = stack.toItemStack().save(a1.startObjectField("Inside"), itemLoader).end();
		}
		return a1;
	}
}