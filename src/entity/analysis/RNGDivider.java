package entity.analysis;

import java.math.*;
import java.util.*;

public abstract class RNGDivider
{
	private static final Random r = new Random();

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

	public RNGDivider rollRNG()
	{
		if(paths.isEmpty())
			return null;
		int limit = paths.stream().mapToInt(e -> (int) e.chance).sum();
		int randomNum = r.nextInt(limit);
		for(int i = 0; i < paths.size(); i++)
		{
			randomNum -= paths.get(i).chance;
			if(randomNum < 0)
				return paths.get(i);
		}
		throw new RuntimeException();
	}
}