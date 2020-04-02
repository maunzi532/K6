package statsystem;

import java.util.*;
import statsystem.content.*;
import text.*;

public final class AttackInfoPart3
{
	private static final int MELT_MODIFIER = 2;
	private static final int CRIT_MODIFIER = 3;

	private final AttackMode3 attackMode;
	private final AttackMode3 attackModeT;
	public final boolean distanceOK;
	public final int advantage;
	public final int healthCost;
	public final int damage;
	public final int meltDamage;
	public final int critDamage;
	public final int meltCritDamage;
	public final int attackCount;
	public final int hitrate;
	public final int critrate;
	public final boolean autohit1;
	public final boolean melting1;

	public AttackInfoPart3(AttackMode3 attackMode, AttackMode3 attackModeT, int distance, AttackSide side)
	{
		this.attackMode = attackMode;
		this.attackModeT = attackModeT;
		distanceOK = Arrays.stream(attackMode.ranges(side)).anyMatch(e -> e == distance);
		advantage = advantage(attackMode.advType, attackModeT.advType);
		healthCost = attackMode.healthCost;
		damage = Math.max(0, attackMode.attackPower - attackModeT.defense(attackMode.defenseType));
		meltDamage = Math.max(0, attackMode.attackPower - (attackModeT.defense(attackMode.defenseType) / MELT_MODIFIER));
		critDamage = damage * CRIT_MODIFIER;
		meltCritDamage = meltDamage + (damage * (CRIT_MODIFIER - 1));
		attackCount = attackCount();
		hitrate = Math.max(0, Math.min(100, attackMode.accuracy - attackModeT.evasion(attackMode.defenseType)));
		critrate = Math.max(0, Math.min(100, attackMode.crit - attackModeT.critProtection));
		autohit1 = advantage >= 0 && attackMode.abilities.contains(XAbility.TWO_HANDED);
		melting1 = attackMode.abilities.contains(XAbility.MELTING);
	}

	private static int advantage(AdvantageType adv, AdvantageType advT)
	{
		if(adv == advT)
			return 0;
		else if(adv == AdvantageType.DEFEND)
			return 1;
		else if(advT == AdvantageType.DEFEND)
			return -1;
		else if(adv == AdvantageType.DAGGER && advT == AdvantageType.AXE)
			return 1;
		else if(advT == AdvantageType.DAGGER && adv == AdvantageType.AXE)
			return -1;
		else if(adv == AdvantageType.SPEAR && advT == AdvantageType.DAGGER)
			return 1;
		else if(advT == AdvantageType.SPEAR && adv == AdvantageType.DAGGER)
			return -1;
		else if(adv == AdvantageType.AXE && advT == AdvantageType.SPEAR)
			return 1;
		else if(advT == AdvantageType.AXE && adv == AdvantageType.SPEAR)
			return -1;
		else
			return 0;
	}

	private int attackCount()
	{
		if(!distanceOK)
			return 0;
		if(attackMode.finalSpeed > attackModeT.finalSpeed)
			return attackMode.attackCount + 1;
		else
			return attackMode.attackCount;
	}

	public CharSequence[] infos()
	{
		CharSequence[] info = new CharSequence[6];
		info[0] = viewHealth();
		if(attackCount > 0)
		{
			info[1] = viewCountXDamage();
			info[2] = viewHitrate();
			info[3] = viewCritrate();
			if(advantage > 0)
				info[4] = "attackinfo.advantage";
		}
		return info;
	}

	public CharSequence[] sideInfos()
	{
		CharSequence[] info = new CharSequence[3];
		if(attackCount > 0)
		{
			info[0] = viewCountXDamage();
			info[1] = viewHitrate();
			info[2] = viewCritrate();
		}
		return info;
	}

	private CharSequence viewHealth()
	{
		if(healthCost > 0)
			return new ArgsText("attackinfo.health.cost", attackMode.stats.currentHealth(), healthCost, attackMode.stats.maxHealth());
		else
			return new ArgsText("attackinfo.health", attackMode.stats.currentHealth(), attackMode.stats.maxHealth());
	}

	private CharSequence viewCountXDamage()
	{
		if(attackCount <= 0)
			return null;
		if(melting1)
		{
			int normalAttackCount = attackCount - 1;
			if(normalAttackCount > 1)
				return new ArgsText("attackinfo.damage.melt", meltDamage, normalAttackCount, damage);
			else
				return new ArgsText("attackinfo.damage.melt.1", meltDamage, damage);
		}
		else
		{
			if(attackCount > 1)
				return new ArgsText("attackinfo.damage", attackCount, damage);
			else
				return new ArgsText("attackinfo.damage.1", damage);
		}
	}

	private CharSequence viewHitrate()
	{
		if(autohit1)
			return new ArgsText("attackinfo.hitrate.autohit1", hitrate);
		else
			return new ArgsText("attackinfo.hitrate", hitrate);
	}

	private CharSequence viewCritrate()
	{
		return new ArgsText("attackinfo.critrate", critrate);
	}
}