package system2;

public class AttackInfoPart2
{
	private final Stats2 attackStats;
	private final AttackMode2 attackMode;
	private final Stats2 defendStats;
	private final AttackMode2 defendMode;
	public final int damage;
	public final int critDamage;
	public final int attackCount;
	public final int hitrate;
	public final int critrate;

	public AttackInfoPart2(Stats2 attackStats, AttackMode2 attackMode, Stats2 defendStats,
			AttackMode2 defendMode)
	{
		this.attackStats = attackStats;
		this.attackMode = attackMode;
		this.defendStats = defendStats;
		this.defendMode = defendMode;
		StatsInfo2 statsInfo = new StatsInfo2(attackStats, attackMode);
		StatsInfo2 statsInfoT = new StatsInfo2(defendStats, defendMode);
		damage = Math.max(0, attackMode.getDamage() + statsInfo.finesse - statsInfoT.defense);
		critDamage = damage * 2;
		attackCount = attackMode instanceof NullMode2 ? 0 : statsInfo.speed > statsInfoT.speed ? attackMode.attackCount() + 1 : attackMode.attackCount();
		hitrate = Math.max(0, Math.min(100, attackMode.getAccuracy() + statsInfo.skill * 10 - statsInfoT.skill * 10));
		critrate = Math.max(0, Math.min(100, attackMode.getCrit() + statsInfo.luck * 5 - statsInfoT.luck * 5));
	}
}