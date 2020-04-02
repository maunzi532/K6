package entity.analysis;

import java.math.*;
import java.util.*;
import text.*;

public class RNGOutcome
{
	public BigInteger chance;
	public long divider;
	public double chanceAsDouble;
	public List<String> compareText;

	public RNGOutcome(BigInteger chance, long divider, List<String> compareText)
	{
		this.chance = chance;
		this.divider = divider;
		chanceAsDouble = chance.doubleValue();
		for(int i = 0; i < divider; i++)
		{
			chanceAsDouble /= 100.0;
		}
		this.compareText = compareText;
	}

	public RNGOutcome(List<? extends RNGOutcome> outcomes)
	{
		chanceAsDouble = outcomes.stream().mapToDouble(e -> e.chanceAsDouble).sum();
		compareText = outcomes.get(0).compareText;
	}

	public CharSequence readableChance()
	{
		if(chanceAsDouble <= 0.0)
			return "calculation.never";
		if(chanceAsDouble >= 1.0)
			return "calculation.always";
		return new ArgsText("calculation.percent", chanceAsDouble * 100);
	}
}