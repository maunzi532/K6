package inv;

public class InvStack
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

	public void commit()
	{
		current += increase - decrease;
		decrease = 0;
		increase = 0;
	}

	public void rollback()
	{
		decrease = 0;
		increase = 0;
	}

	public int maxDecrease()
	{
		return current - decrease;
	}

	public int maxIncrease(int limit)
	{
		return limit - current - increase;
	}

	public void decrease(int by)
	{
		decrease += by;
	}

	public void increase(int by)
	{
		increase += by;
	}

	public boolean removable()
	{
		return current == 0 && decrease == 0 && increase == 0;
	}
}