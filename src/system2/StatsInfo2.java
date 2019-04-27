package system2;

import java.util.*;

public class StatsInfo2
{
	private final Stats2 stats;
	private final AttackMode2 attackMode;
	public final int exhausted;
	public final int weighedDown;
	public final int strength;
	public final int finesse;
	public final int skill;
	public final int speed;
	public final int luck;
	public final int defense;
	public final int toughness;
	public final List<Ability2> abilities;

	public StatsInfo2(Stats2 stats, AttackMode2 attackMode)
	{
		this.stats = stats;
		this.attackMode = attackMode;
		exhausted = stats.getExhaustion();
		strength = Math.max(0, stats.getStrength() - exhausted);
		weighedDown = Math.max(0, attackMode.getHeavy() - strength);
		finesse = Math.max(0, stats.getFinesse() - exhausted - weighedDown);
		skill = Math.max(0, stats.getSkill() - exhausted);
		speed = Math.max(0, stats.getSpeed() - exhausted - weighedDown);
		luck = stats.getLuck();
		defense = Math.max(0, stats.getDefense() - exhausted);
		toughness = stats.getToughness();
		abilities = attackMode.getAllAbilities(stats);
	}
}