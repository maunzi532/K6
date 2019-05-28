package entity.analysis;

import java.math.*;
import java.text.*;
import java.util.*;

public class RNGOutcome
{
	private static final NumberFormat f1 = NumberFormat.getPercentInstance();

	private BigInteger chance;
	private long divider;
	private BigInteger divider2;
	private double chanceAsDouble;
	private int moveScore;
	public List<String> compareText;

	public RNGOutcome(BigInteger chance, long divider, int moveScore, List<String> compareText)
	{
		this.chance = chance;
		this.divider = divider;
		divider2 = BigInteger.ONE;
		chanceAsDouble = chance.doubleValue();
		for(int i = 0; i < divider; i++)
		{
			divider2.multiply(BigInteger.valueOf(100));
			chanceAsDouble /= 100;
		}
		this.moveScore = moveScore;
		this.compareText = compareText;
	}

	public RNGOutcome(List<RNGOutcome> outcomes)
	{
		chance = outcomes.stream().map(e -> e.chance).reduce(BigInteger.ONE, BigInteger::multiply);
		divider = outcomes.stream().mapToLong(e -> e.divider).max().orElse(0);
		divider2 = BigInteger.ONE;
		for(int i = 0; i < divider; i++)
		{
			divider2.multiply(BigInteger.valueOf(100));
		}
		chanceAsDouble = outcomes.stream().mapToDouble(e -> e.chanceAsDouble).sum();
		moveScore = outcomes.get(0).moveScore;
		compareText = outcomes.get(0).compareText;
	}

	public String readableChance()
	{
		if(chance.equals(BigInteger.ZERO))
			return "Never";
		if(chance.equals(divider2))
			return "Always";
		return f1.format(chanceAsDouble);
	}
}