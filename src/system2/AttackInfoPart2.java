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
	public final int cost;
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
		attackCount = attackMode instanceof NullMode2 ? 0 : attackMode.attackCount() +
				(statsInfo.speed > statsInfoT.speed ? 1 : 0) + (abilities.contains(Ability2.FAST) ? 1 : 0);
		if(attackMode.magical())
		{
			damage = Math.max(0, attackMode.getDamage() + statsInfo.skill - statsInfoT.magicDef);
			cost = attackCount > 0 ? Math.max(0, attackMode.getHeavy() - statsInfo.strength) : 0;
			hitrate = Math.max(0, Math.min(100, attackMode.getAccuracy() + statsInfo.finesse * 5 - statsInfoT.finesse * 4));
			autohit1 = false;
		}
		else
		{
			damage = Math.max(0, attackMode.getDamage() + statsInfo.finesse - statsInfoT.defense);
			cost = 0;
			hitrate = Math.max(0, Math.min(100, attackMode.getAccuracy() + statsInfo.skill * 5 - statsInfoT.skill * 4));
			autohit1 = advantage >= 0 && abilities.contains(Ability2.TWO_HANDED);
		}
		critDamage = damage * 2;
		critrate = Math.max(0, Math.min(100, attackMode.getCrit() + statsInfo.luck * 5 - statsInfoT.luck * 5));
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
		if(cost > 0)
			infos[0] = attackStats.getCurrentHealth() + "(-" + cost + ")/" + attackStats.getToughness();
		else
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