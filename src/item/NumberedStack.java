package item;

import text.*;

public record NumberedStack(Item item, int count, boolean limited, String tag, int num)
{
	public static NumberedStack create(ItemStack stack, int num)
	{
		return new NumberedStack(stack.item(), stack.count(), true, null, num);
	}

	public static NumberedStack unlimited(Item item, int count, int num)
	{
		return new NumberedStack(item, count, false, null, num);
	}

	public static NumberedStack tagged(TagStack stack, int num)
	{
		return new NumberedStack(stack.items().item(), stack.items().count(), true, stack.tag(), num);
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