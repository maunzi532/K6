package system2;

import java.util.*;
import system2.content.*;

public class AttackInfoPart3
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

	public AttackInfoPart3(AttackMode3 attackMode, AttackMode3 attackModeT, int distance, boolean counter)
	{
		this.attackMode = attackMode;
		this.attackModeT = attackModeT;
		distanceOK = Arrays.stream(counter ? attackMode.counter : attackMode.ranges).anyMatch(e -> e == distance);
		advantage = advantage(attackMode.advType, attackModeT.advType);
		healthCost = attackMode.healthCost;
		damage = Math.max(0, attackMode.attackPower - attackModeT.defense(attackMode.magical));
		meltDamage = Math.max(0, attackMode.attackPower - (attackModeT.defense(attackMode.magical) / MELT_MODIFIER));
		critDamage = damage * CRIT_MODIFIER;
		meltCritDamage = meltDamage + (damage * (CRIT_MODIFIER - 1));
		attackCount = attackCount();
		hitrate = Math.max(0, Math.min(100, attackMode.accuracy - attackModeT.evasion(attackMode.magical)));
		critrate = Math.max(0, Math.min(100, attackMode.crit - attackModeT.critProtection));
		autohit1 = advantage >= 0 && attackMode.abilities.contains(Ability2.TWO_HANDED);
		melting1 = attackMode.abilities.contains(Ability2.MELTING);
	}

	public String[] infos()
	{
		return new String[0];
	}

	public String[] sideInfos()
	{
		return new String[0];
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
}