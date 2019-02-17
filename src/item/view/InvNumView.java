package item.view;

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

	public String baseAndCurrentWithLimit()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(base);
		if(changed != base)
			sb.append(" -> ").append(changed);
		if(limit >= 0)
			sb.append(" / ").append(limit);
		return sb.toString();
	}

	public static String except1(int num)
	{
		if(num == 1)
			return null;
		else
			return String.valueOf(num);
	}
}