package item4;

import text.*;

public record NumberedStack4(Item4 item, int count, boolean limited, boolean keep, String tag, int num)
{
	public static NumberedStack4 create(ItemStack4 stack, int num)
	{
		return new NumberedStack4(stack.item(), stack.count(), true, false, null, num);
	}

	public static NumberedStack4 unlimited(Item4 item, int count, boolean keep, int num)
	{
		return new NumberedStack4(item, count, false, keep, null, num);
	}

	public static NumberedStack4 tagged(TagStack4 stack, boolean keep, int num)
	{
		return new NumberedStack4(stack.items().item(), stack.items().count(), true, keep, stack.tag(), num);
	}

	public CharSequence viewText()
	{
		if(tag != null)
		{
			if(!limited)
				return new ArgsText("itemview.base.tag", count, tag);
			int limit = item.stackLimit();
			if(count == 1 && limit == 1)
				return new ArgsText("itemview.one.tag", tag);
			else
				return new ArgsText("itemview.limit.tag", count, limit, tag);
		}
		else
		{
			if(!limited)
				return new ArgsText("itemview.base", count);
			int limit = item.stackLimit();
			if(count == 1 && limit == 1)
				return new ArgsText("itemview.one");
			else
				return new ArgsText("itemview.limit", count, limit);
		}
	}
}