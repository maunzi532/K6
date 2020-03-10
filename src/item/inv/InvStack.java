package item.inv;

import item.*;
import java.util.Optional;

public class InvStack implements Inv0
{
	public final Item item;
	private int current;
	private int decrease;
	private int increase;

	public InvStack(ItemStack items)
	{
		item = items.item;
		current = 0;
		decrease = 0;
		increase = items.count;
	}

	public InvStack(InvStack copy)
	{
		item = copy.item;
		current = copy.current;
		decrease = 0;
		increase = 0;
	}

	public ItemStack toItemStack()
	{
		return new ItemStack(item, current);
	}

	public int getCountC()
	{
		return current;
	}

	public int getCountX()
	{
		return current + increase - decrease;
	}

	public boolean canProvideX()
	{
		return current - decrease > 0;
	}

	public int getIncreasedX()
	{
		return current + increase;
	}

	public boolean removable()
	{
		return current == 0 && decrease == 0 && increase == 0;
	}

	@Override
	public boolean ok()
	{
		return current - decrease >= 0;
	}

	@Override
	public void commit()
	{
		current += increase - decrease;
		decrease = 0;
		increase = 0;
	}

	@Override
	public void rollback()
	{
		decrease = 0;
		increase = 0;
	}

	@Override
	public boolean canGive(ItemStack itemStack, boolean unlimited)
	{
		if(item.equals(itemStack.item))
		{
			return unlimited || current - decrease >= itemStack.count;
		}
		return false;
	}

	@Override
	public boolean give(ItemStack itemStack, boolean unlimited)
	{
		if(canGive(itemStack, unlimited))
		{
			decrease += itemStack.count;
			return true;
		}
		return false;
	}

	@Override
	public Optional<ItemStack> wouldProvide(ItemStack itemStack, boolean unlimited)
	{
		if(itemStack.item.canContain(item) && (unlimited || current - decrease >= itemStack.count))
		{
			return Optional.of(new ItemStack(item, itemStack.count));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ItemStack> provide(ItemStack itemStack, boolean unlimited)
	{
		Optional<ItemStack> provided = wouldProvide(itemStack, unlimited);
		if(provided.isPresent())
		{
			decrease += itemStack.count;
		}
		return provided;
	}

	@Override
	public boolean canAdd(ItemStack itemStack, boolean unlimited)
	{
		return item.equals(itemStack.item);
	}

	@Override
	public boolean add(ItemStack itemStack, boolean unlimited)
	{
		if(canAdd(itemStack, unlimited))
		{
			increase += itemStack.count;
			return true;
		}
		return false;
	}
}