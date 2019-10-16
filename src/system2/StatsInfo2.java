package system2;

import java.util.*;

public class StatsInfo2
{
	private final Stats2 stats;
	private final AttackMode2 attackMode;
	public final int exhausted;
	public final int tooHeavy;
	public final int adapt;
	public final int strength;
	public final int finesse;
	public final int skill;
	public final int speed;
	public final int luck;
	public final int defense;
	public final int evasion;
	public final int toughness;
	public final List<Ability2> abilities;

	public StatsInfo2(Stats2 stats, AttackMode2 attackMode)
	{
		this.stats = stats;
		this.attackMode = attackMode;
		exhausted = stats.getExhaustion();
		strength = Math.max(0, stats.getStrength() - exhausted);
		if(attackMode.getAdaptiveType() == AdaptiveType.COST)
		{
			tooHeavy = 0;
			adapt = 0;
		}
		else
		{
			int minWeight = attackMode.getHeavy();
			tooHeavy = Math.max(0, minWeight - strength);
			adapt = Math.max(0, Math.min(attackMode.getAdaptive(), strength - minWeight));
		}
		int adaptFinesse = attackMode.getAdaptiveType() == AdaptiveType.FINESSE ? adapt : 0;
		finesse = Math.max(0, stats.getFinesse() + adaptFinesse - exhausted - tooHeavy);
		int adaptSkill = attackMode.getAdaptiveType() == AdaptiveType.SKILL ? adapt : 0;
		skill = Math.max(0, stats.getSkill() + adaptSkill - exhausted);
		int adaptSpeed = attackMode.getAdaptiveType() == AdaptiveType.SPEED ? adapt : 0;
		speed = Math.max(0, stats.getSpeed() + adaptSpeed - attackMode.getSlow() - exhausted - tooHeavy);
		int adaptLuck = attackMode.getAdaptiveType() == AdaptiveType.LUCK ? adapt : 0;
		luck = stats.getLuck() + adaptLuck;
		int adaptDefense = attackMode.getAdaptiveType() == AdaptiveType.DEFENSE ? adapt : 0;
		defense = Math.max(0, stats.getDefense() + adaptDefense - exhausted);
		int adaptEvasion = attackMode.getAdaptiveType() == AdaptiveType.EVASION ? adapt : 0;
		evasion = Math.max(0, stats.getEvasion() + adaptEvasion - exhausted);
		toughness = stats.getToughness();
		abilities = attackMode.getAllAbilities(stats);
	}
}