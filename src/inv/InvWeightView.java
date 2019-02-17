package inv;

public class InvWeightView
{
	public final int base;
	public final int changed;
	public final int limit;

	public InvWeightView(int base, int changed, int limit)
	{
		this.base = base;
		this.changed = changed;
		this.limit = limit;
	}

	public InvWeightView(int base, int changed)
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
		if(limit < 0)
			return base + " -> " + changed;
		else
			return base + " -> " + changed + " / " + limit;
	}
}