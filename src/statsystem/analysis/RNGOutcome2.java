package statsystem.analysis;

import entity.analysis.*;
import java.math.*;
import java.util.*;
import statsystem.*;

public final class RNGOutcome2 extends RNGOutcome
{
	public int damageTaken;
	public int remainingHealth;
	public int maxHealth;
	public boolean lose;
	public int damageDealtT;
	public int remainingHealthT;
	public int maxHealthT;
	public boolean win;

	public RNGOutcome2(BigInteger chance, long divider,
			List<String> compareText, AttackInfo sourceaI, int health, int healthT)
	{
		super(chance, divider, compareText);
		damageTaken = sourceaI.stats.currentHealth() - health;
		remainingHealth = health;
		maxHealth = sourceaI.stats.maxHealth();
		lose = health <= 0;
		damageDealtT = sourceaI.statsT.currentHealth() - healthT;
		remainingHealthT = healthT;
		maxHealthT = sourceaI.statsT.maxHealth();
		win = healthT <= 0;
	}
}