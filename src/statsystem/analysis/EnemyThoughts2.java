package statsystem.analysis;

import entity.analysis.*;
import java.util.*;

public final class EnemyThoughts2
{
	private double loseChance;
	private double avgDamageTakenInMaxHealth;
	private double avgHealthRemainingInMaxHealth;
	private double winChance;
	private double avgDamageDealtInMaxHealth;
	private double avgDownToInMaxHealth;

	public EnemyThoughts2(List<RNGOutcome> outcomes1)
	{
		List<RNGOutcome2> outcomes = new ArrayList<>();
		for(RNGOutcome outcome : outcomes1)
		{
			outcomes.add((RNGOutcome2) outcome);
		}
		loseChance = outcomes.stream().filter(e -> e.lose).mapToDouble(e -> e.chanceAsDouble).sum();
		avgDamageTakenInMaxHealth = outcomes.stream().mapToDouble(e -> e.damageTaken / (double) e.maxHealth).sum() / outcomes.size();
		avgHealthRemainingInMaxHealth = outcomes.stream().mapToDouble(e -> e.remainingHealth / (double) e.maxHealth).sum() / outcomes.size();
		winChance = outcomes.stream().filter(e -> e.win).mapToDouble(e -> e.chanceAsDouble).sum();
		avgDamageDealtInMaxHealth = outcomes.stream().mapToDouble(e -> e.damageDealtT / (double) e.maxHealthT).sum() / outcomes.size();
		avgDownToInMaxHealth = outcomes.stream().mapToDouble(e -> e.remainingHealthT / (double) e.maxHealthT).sum() / outcomes.size();
		//System.out.println(toString());
	}

	public double score()
	{
		double score = 8;
		if(winChance > 0)
			score += 1;
		score += winChance * 10;
		score += avgDamageDealtInMaxHealth * 5;
		score -= avgDownToInMaxHealth * 2;
		if(loseChance > 0)
			score -= 0.5;
		score -= loseChance * 2;
		score -= avgDamageTakenInMaxHealth * 3;
		score += avgHealthRemainingInMaxHealth * 2;
		//System.out.println(score);
		return score;
	}

	@Override
	public String toString()
	{
		return "EnemyThoughts2{" +
				"loseChance=" + loseChance +
				", avgDamageTakenInMaxHealth=" + avgDamageTakenInMaxHealth +
				", avgHealthRemainingInMaxHealth=" + avgHealthRemainingInMaxHealth +
				", winChance=" + winChance +
				", avgDamageDealtInMaxHealth=" + avgDamageDealtInMaxHealth +
				", avgDownToInMaxHealth=" + avgDownToInMaxHealth +
				'}';
	}
}