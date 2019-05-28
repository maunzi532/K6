package entity.analysis;

import java.math.*;
import java.util.*;

public abstract class RNGDivider
{
	public RNGDivider prev;
	public long chance;
	public long divider;
	public List<RNGDivider> paths;

	public RNGDivider(RNGDivider prev, long chance, long divider)
	{
		this.prev = prev;
		this.chance = chance;
		this.divider = divider;
		paths = new ArrayList<>();
	}

	public abstract void build();

	public abstract RNGOutcome asOutcome();

	public BigInteger chanceC()
	{
		return prev != null ? prev.chanceC().multiply(BigInteger.valueOf(chance)) : BigInteger.valueOf(chance);
	}

	public long dividerC()
	{
		return prev != null ? prev.dividerC() + divider : divider;
	}
}