package system2;

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
	public final int endurance;

	public StatsInfo2(Stats2 stats, AttackMode2 attackMode)
	{
		this.stats = stats;
		this.attackMode = attackMode;
		exhausted = Math.max(0, stats.getAccumulatedDamage() / stats.getEndurance());
		strength = Math.max(0, stats.getStrength() - exhausted);
		weighedDown = Math.max(0, attackMode.getHeavy() - strength);
		finesse = Math.max(0, stats.getFinesse() - exhausted - weighedDown);
		skill = Math.max(0, stats.getSkill() - exhausted);
		speed = Math.max(0, stats.getSpeed() - exhausted - weighedDown);
		luck = stats.getLuck();
		defense = Math.max(0, stats.getFinesse() - exhausted);
		toughness = stats.getToughness();
		endurance = stats.getEndurance();
	}
}