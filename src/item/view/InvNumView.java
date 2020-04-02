package item.view;

import text.*;

public class InvNumView
{
	public final int base;
	public final int changed;
	public final int limit;

	public InvNumView(int base, int changed, int limit)
	{
		this.base = base;
		this.changed = changed;
		this.limit = limit;
	}

	public InvNumView(int base, int changed)
	{
		this.base = base;
		this.changed = changed;
		limit = -1;
	}

	public CharSequence currentWithLimit()
	{
		if(limit >= 0)
			return new ArgsText("itemview.limit", changed, limit);
		else
			return new ArgsText("itemview.base", changed);
	}

	public CharSequence baseAndCurrentWithLimit()
	{
		if(changed != base)
		{
			if(limit >= 0)
				return new ArgsText("itemview.changedandlimit", base, changed, limit);
			else
				return new ArgsText("itemview.changed", base, changed);
		}
		else
		{
			return currentWithLimit();
		}
	}
}