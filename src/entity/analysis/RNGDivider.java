package entity.analysis;

import java.util.*;

public abstract class RNGDivider
{
	public long chance;
	public List<RNGDivider> paths;
	public long divider;

	public RNGDivider(long chance)
	{
		this.chance = chance;
		paths = new ArrayList<>();
		divider = 1;
	}

	public abstract void build();
}