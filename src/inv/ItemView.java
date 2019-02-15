package inv;

public class ItemView
{
	public final Item item;
	public final int base;
	public final int changed;
	public final int limit;

	public ItemView(Item item, int base, int changed, int limit)
	{
		this.item = item;
		this.base = base;
		this.changed = changed;
		this.limit = limit;
	}

	public ItemView(Item item, int base, int changed)
	{
		this.item = item;
		this.base = base;
		this.changed = changed;
		limit = -1;
	}

	public ItemView(ItemStack items, int limit)
	{
		item = items.item;
		base = items.count;
		changed = items.count;
		this.limit = limit;
	}

	public ItemView(ItemStack items)
	{
		item = items.item;
		base = items.count;
		changed = items.count;
		limit = -1;
	}

	public int changeType()
	{
		return Integer.compare(changed, base);
	}

	public String currentWithLimit()
	{
		if(limit < 0)
			return String.valueOf(changed);
		else
			return changed + " / " + limit;
	}

	public static String except1(int num)
	{
		if(num == 1)
			return null;
		else
			return String.valueOf(num);
	}
}