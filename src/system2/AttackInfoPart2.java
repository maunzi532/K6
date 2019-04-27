package system2;

import java.util.*;

public class AttackInfoPart2
{
	private final Stats2 attackStats;
	private final AttackMode2 attackMode;
	private final Stats2 defendStats;
	private final AttackMode2 defendMode;
	public final List<Ability2> abilities;
	public final int advantage;
	public final int damage;
	public final int critDamage;
	public final int attackCount;
	public final int hitrate;
	public final int critrate;
	public final boolean autohit1;

	public AttackInfoPart2(Stats2 attackStats, AttackMode2 attackMode, Stats2 defendStats,
			AttackMode2 defendMode)
	{
		this.attackStats = attackStats;
		this.attackMode = attackMode;
		this.defendStats = defendStats;
		this.defendMode = defendMode;
		StatsInfo2 statsInfo = new StatsInfo2(attackStats, attackMode);
		StatsInfo2 statsInfoT = new StatsInfo2(defendStats, defendMode);
		abilities = statsInfo.abilities;
		advantage = advantage(attackMode.getAdvType(), defendMode.getAdvType());
		damage = Math.max(0, attackMode.getDamage() + statsInfo.finesse - statsInfoT.defense);
		critDamage = damage * 2;
		attackCount = attackMode instanceof NullMode2 ? 0 : statsInfo.speed > statsInfoT.speed ? attackMode.attackCount() + 1 : attackMode.attackCount();
		hitrate = Math.max(0, Math.min(100, attackMode.getAccuracy() + statsInfo.skill * 10 - statsInfoT.skill * 10));
		critrate = Math.max(0, Math.min(100, attackMode.getCrit() + statsInfo.luck * 5 - statsInfoT.luck * 5));
		autohit1 = advantage >= 0 && abilities.contains(Ability2.TWO_HANDED);
	}

	private int advantage(int adv, int advT)
	{
		if(adv < 1 || advT < 1 || adv > 3 || advT > 3)
			return 0;
		int diff = adv - advT;
		if(diff > 1)
			return -1;
		if(diff < -1)
			return 1;
		return diff;
	}

	public String[] infos()
	{
		String[] infos = new String[5];
		infos[0] = attackStats.getCurrentHealth() + "/" + attackStats.getToughness();
		if(attackCount > 0)
		{
			infos[1] = damage + "x" + attackCount;
			infos[2] = hitrate + (autohit1 ? "*" : "");
			infos[3] = String.valueOf(critrate);
			infos[4] = advantage > 0 ? "+" : "";
		}
		return infos;
	}
}