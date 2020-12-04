package item4;

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
			return item.name() + " x" + count + "\n" + tag;
		else
			return item.name() + " x" + count;
		/*if(limited)
		{
			int limit = item.stackLimit();
			if(count == limit)
			{
				if(count == 1)
					return new ArgsText("itemview.one");
				else
					return new ArgsText("itemview.base", count);
			}
			else
			{
				return new ArgsText("itemview.limit", count, limit);
			}
		}
		else
		{
			return new ArgsText("itemview.base", count);
		}*/
	}
}