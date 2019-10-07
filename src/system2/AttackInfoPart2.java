package system2;

import java.util.*;
import system2.content.*;

public class AttackInfoPart2
{
	private static final int MELT_MODIFIER = 2;
	private static final int CRIT_MODIFIER = 3;
	private static final int HITRATE_PER_STAT = 2;
	private static final int CRITRATE_PER_STAT = 2;

	private final Stats2 attackStats;
	private final AttackMode2 attackMode;
	private final Stats2 defendStats;
	private final AttackMode2 defendMode;
	public final List<Ability2> abilities;
	public final int advantage;
	public final int cost;
	public final int damage;
	public final int meltDamage;
	public final int critDamage;
	public final int meltCritDamage;
	public final int attackCount;
	public final int hitrate;
	public final int critrate;
	public final boolean autohit1;
	public final boolean melting1;

	public AttackInfoPart2(Stats2 attackStats, AttackMode2 attackMode, boolean rangeOk,
			Stats2 defendStats, AttackMode2 defendMode, boolean rangeOkD)
	{
		this.attackStats = attackStats;
		this.attackMode = attackMode;
		this.defendStats = defendStats;
		this.defendMode = defendMode;
		StatsInfo2 statsInfo = new StatsInfo2(attackStats, attackMode);
		StatsInfo2 statsInfoT = new StatsInfo2(defendStats, defendMode);
		abilities = statsInfo.abilities;
		advantage = advantage(attackMode.getAdvType(), defendMode.getAdvType());
		attackCount = calcAttackCount(attackMode, rangeOk, statsInfo, statsInfoT);
		melting1 = abilities.contains(Ability2.MELTING);
		if(attackMode.magical())
		{
			damage = Math.max(0, attackMode.getDamage() + statsInfo.skill - statsInfoT.evasion);
			meltDamage = Math.max(0, attackMode.getDamage() + statsInfo.skill - statsInfoT.evasion / MELT_MODIFIER);
			cost = attackCount > 0 ? Math.max(0, attackMode.getHeavy() - statsInfo.strength) : 0;
			hitrate = Math.max(0, Math.min(100, attackMode.getAccuracy() + statsInfo.finesse * HITRATE_PER_STAT - statsInfoT.luck * HITRATE_PER_STAT));
			autohit1 = false;
		}
		else
		{
			damage = Math.max(0, attackMode.getDamage() + statsInfo.finesse - statsInfoT.defense);
			meltDamage = Math.max(0, attackMode.getDamage() + statsInfo.finesse - statsInfoT.defense / MELT_MODIFIER);
			cost = 0;
			hitrate = Math.max(0, Math.min(100, attackMode.getAccuracy() + statsInfo.skill * HITRATE_PER_STAT - statsInfoT.evasion * HITRATE_PER_STAT));
			autohit1 = advantage >= 0 && abilities.contains(Ability2.TWO_HANDED);
		}
		critDamage = damage * CRIT_MODIFIER;
		meltCritDamage = meltDamage + (damage * (CRIT_MODIFIER - 1));
		critrate = Math.max(0, Math.min(100, attackMode.getCrit() + statsInfo.luck * CRITRATE_PER_STAT - statsInfoT.luck * CRITRATE_PER_STAT));
	}

	private int calcAttackCount(AttackMode2 attackMode, boolean rangeOk, StatsInfo2 statsInfo, StatsInfo2 statsInfoT)
	{
		if(!rangeOk)
			return 0;
		int atc = attackMode.attackCount();
		if(abilities.contains(Ability2.FAST))
			atc++;
		if(statsInfo.speed > statsInfoT.speed)
			atc++;
		return atc;
	}

	private int advantage(AdvantageType adv, AdvantageType advT)
	{
		if(adv == advT)
			return 0;
		if(adv == AdvantageType.DEFEND)
			return 1;
		if(advT == AdvantageType.DEFEND)
			return -1;
		if(adv == AdvantageType.DAGGER && advT == AdvantageType.AXE)
			return 1;
		if(advT == AdvantageType.DAGGER && adv == AdvantageType.AXE)
			return -1;
		if(adv == AdvantageType.SPEAR && advT == AdvantageType.DAGGER)
			return 1;
		if(advT == AdvantageType.SPEAR && adv == AdvantageType.DAGGER)
			return -1;
		if(adv == AdvantageType.AXE && advT == AdvantageType.SPEAR)
			return 1;
		if(advT == AdvantageType.AXE && adv == AdvantageType.SPEAR)
			return -1;
		return 0;
	}

	public String[] infos()
	{
		String[] infos = new String[5];
		if(cost > 0)
			infos[0] = attackStats.getCurrentHealth() + "(-" + cost + ")/" + attackStats.maxHealth();
		else
			infos[0] = attackStats.getCurrentHealth() + "/" + attackStats.maxHealth();
		if(attackCount > 0)
		{
			if(melting1)
				infos[1] = meltDamage + "+" + damage + (attackCount > 2 ? "x" + (attackCount - 1) : "");
			else
				infos[1] = damage + (attackCount > 1 ? "x" + attackCount : "");
			infos[2] = hitrate + (autohit1 ? "*" : "");
			infos[3] = String.valueOf(critrate);
			infos[4] = advantage > 0 ? "+" : "";
		}
		return infos;
	}

	public String[] sideInfos()
	{
		String[] infos = new String[5];
		if(cost > 0)
			infos[0] = attackStats.getCurrentHealth() + "(-" + cost + ")/" + attackStats.maxHealth();
		else
			infos[0] = attackStats.getCurrentHealth() + "/" + attackStats.maxHealth();
		if(attackCount > 0)
		{
			if(melting1)
				infos[1] = meltDamage + "+" + damage + (attackCount > 2 ? "x" + (attackCount - 1) : "");
			else
				infos[1] = damage + (attackCount > 1 ? "x" + attackCount : "");
			infos[2] = hitrate + (autohit1 ? "*" : "");
			infos[3] = String.valueOf(critrate);
			infos[4] = advantage > 0 ? "+" : "";
		}
		return infos;
	}
}