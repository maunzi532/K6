package inv;

public class InvStack
{
	public final Item item;
	private int current = 0;
	private int decrease = 0;
	private int increase = 0;
	public final int limit;

	public InvStack(Item item)
	{
		this.item = item;
		limit = -1;
	}

	public InvStack(Item item, int limit)
	{
		this.item = item;
		this.limit = limit;
	}

	public InvStack(ItemStack itemStack)
	{
		item = itemStack.item;
		limit = itemStack.count;
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
	}

	public void rollback()
	{
		decrease = 0;
		increase = 0;
	}

	public boolean canDecrease(int by)
	{
		return current - decrease - by >= 0;
	}

	public int maxDecrease()
	{
		return current - decrease;
	}

	public boolean canIncrease(int by)
	{
		if(limit < 0)
			return true;
		return current + increase + by <= limit;
	}

	public int maxIncrease(int limited)
	{
		return limit >= 0 ? limit - current - increase : limited;
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
		return limit < 0 && current == 0 && decrease == 0 && increase == 0;
	}
}