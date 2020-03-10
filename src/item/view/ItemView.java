package item.view;

import item.*;

public class ItemView extends InvNumView
{
	public final Item item;

	public ItemView(Item item, int base, int changed, int limit)
	{
		super(base, changed, limit);
		this.item = item;
	}

	public ItemView(Item item, int base, int changed)
	{
		super(base, changed);
		this.item = item;
	}

	public ItemView(ItemStack items, int limit)
	{
		super(items.count, items.count, limit);
		item = items.item;
	}

	public ItemView(ItemStack items)
	{
		super(items.count, items.count);
		item = items.item;
	}
}