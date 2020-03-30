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

	protected RNGDivider(RNGDivider prev, long chance, long divider)
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

	private int chanceForDivider(int maxDivider)
	{
		int chance2 = (int) chance;
		for(int div2 = (int) divider; div2 < maxDivider; div2++)
		{
			chance2 *= 100;
		}
		return chance2;
	}

	public RNGDivider rollRNG()
	{
		if(paths.isEmpty())
			return null;
		int maxDivider = paths.stream().mapToInt(e -> (int) e.divider).max().orElseThrow();
		int limit = paths.stream().mapToInt(e -> e.chanceForDivider(maxDivider)).sum();
		int randomNum = r.nextInt(limit);
		for(RNGDivider path : paths)
		{
			randomNum -= path.chanceForDivider(maxDivider);
			if(randomNum < 0)
				return path;
		}
		throw new RuntimeException("RollRNG Error");
	}
}