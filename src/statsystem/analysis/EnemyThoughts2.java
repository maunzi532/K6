package statsystem.analysis;

import java.util.*;

public final class EnemyThoughts2
{
	private double loseChance;
	private double avgDamageTakenInMaxHealth;
	private double avgHealthRemainingInMaxHealth;
	private double winChance;
	private double avgDamageDealtInMaxHealth;
	private double avgDownToInMaxHealth;

	public EnemyThoughts2(List<RNGOutcome2> outcomes)
	{
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
		double score = 8.0;
		if(winChance > 0.0)
			score += 1.0;
		score += winChance * 10.0;
		score += avgDamageDealtInMaxHealth * 5.0;
		score -= avgDownToInMaxHealth * 2.0;
		if(loseChance > 0.0)
			score -= 0.5;
		score -= loseChance * 2.0;
		score -= avgDamageTakenInMaxHealth * 3.0;
		score += avgHealthRemainingInMaxHealth * 2.0;
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