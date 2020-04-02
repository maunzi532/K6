package statsystem.analysis;

import entity.analysis.*;
import java.util.*;
import java.util.stream.*;
import statsystem.*;

public final class RNGDivider2 extends RNGDivider<RNGOutcome2>
{
	private final AttackInfo attackInfo;
	private final int health1;
	private final int health2;
	private final int attacks1;
	private final int attacks2;
	private final int attacked1;
	private final int attacked2;
	private final int crits1;
	private final int crits2;
	private final boolean end;
	private final List<SidedAttackAnalysisEvent> events;

	public RNGDivider2(AttackInfo attackInfo)
	{
		super(null, 1, 0);
		this.attackInfo = attackInfo;
		health1 = attackInfo.stats.currentHealth();
		health2 = attackInfo.statsT.currentHealth();
		attacks1 = attackInfo.calc.attackCount;
		attacks2 = attackInfo.calcT.attackCount;
		attacked1 = 0;
		attacked2 = 0;
		crits1 = 0;
		crits2 = 0;
		end = false;
		events = List.of();
	}

	private RNGDivider2(RNGDivider2 prev, long chance, long divider, AttackInfo attackInfo, int health1, int health2,
			int attacks1, int attacks2, int attacked1, int attacked2, int crits1, int crits2, boolean end,
			List<SidedAttackAnalysisEvent> events)
	{
		super(prev, chance, divider);
		this.attackInfo = attackInfo;
		this.health1 = health1;
		this.health2 = health2;
		this.attacks1 = attacks1;
		this.attacks2 = attacks2;
		this.attacked1 = attacked1;
		this.attacked2 = attacked2;
		this.crits1 = crits1;
		this.crits2 = crits2;
		this.end = end;
		this.events = events;
	}

	public List<SidedAttackAnalysisEvent> getEvents()
	{
		return events;
	}

	public AttackInfo getAttackInfo()
	{
		return attackInfo;
	}

	@Override
	public void build()
	{
		if(end)
			return;
		boolean canAttack1 = attacked1 < attacks1;
		boolean canAttack2 = attacked2 < attacks2;
		if(canAttack1 && (attacked1 <= attacked2 || !canAttack2))
		{
			//1
			List<SidedAttackAnalysisEvent> events2 = new ArrayList<>();
			int health1a = health1;
			if(attacked1 == 0 && attackInfo.calc.healthCost > 0)
			{
				if(attackInfo.calc.healthCost >= health1)
				{
					//cost too high
					paths.add(new RNGDivider2(this, 1, 0, attackInfo, health1a,
							health2, 0, attacks2, 0, attacked2, crits1, crits2, false, events2));
					return;
				}
				else
				{
					events2.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.HEALTHCOST, AttackSide.INITIATOR));
					health1a -= attackInfo.calc.healthCost;
				}
			}
			events2.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.ATTACK, AttackSide.INITIATOR));
			int hitrate = attacked1 == 0 && attackInfo.calc.autohit1 ? 100 : attackInfo.calc.hitrate;
			int critrate = crits1 > 0 ? 0 : attackInfo.calc.critrate;
			if(hitrate < 100)
			{
				//miss
				List<SidedAttackAnalysisEvent> events3 = new ArrayList<>(events2);
				events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.MISS, AttackSide.INITIATOR));
				paths.add(new RNGDivider2(this, 100 - hitrate, 1, attackInfo, health1a,
						health2, attacks1, attacks2, attacked1 + 1, attacked2, crits1, crits2, false, events3));
			}
			if(hitrate > 0 && critrate < 100)
			{
				//hit
				int damageDealt = attackInfo.calc.melting1 && attacked1 == 0 ? attackInfo.calc.meltDamage : attackInfo.calc.damage;
				List<SidedAttackAnalysisEvent> events3 = new ArrayList<>(events2);
				if(damageDealt > 0)
				{
					if(attackInfo.calc.melting1 && attacked1 == 0)
						events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.MELT, AttackSide.INITIATOR));
					else
						events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.HIT, AttackSide.INITIATOR));
				}
				else
					events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.NODAMAGE, AttackSide.INITIATOR));
				int remaining2 = health2 - damageDealt;
				if(remaining2 <= 0)
					events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.DEFEATED, AttackSide.INITIATOR));
				paths.add(new RNGDivider2(this, hitrate * (100L - critrate), 2, attackInfo, health1a,
						Math.max(0, remaining2), attacks1, attacks2, attacked1 + 1, attacked2, crits1,
						crits2, remaining2 <= 0, events3));
			}
			if(hitrate > 0 && critrate > 0)
			{
				//crit
				int damageDealt = attackInfo.calc.melting1 && attacked1 == 0 ? attackInfo.calc.meltCritDamage : attackInfo.calc.critDamage;
				List<SidedAttackAnalysisEvent> events3 = new ArrayList<>(events2);
				if(damageDealt > 0)
				{
					if(attackInfo.calc.melting1 && attacked1 == 0)
						events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.MELTCRIT, AttackSide.INITIATOR));
					else
						events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.CRIT, AttackSide.INITIATOR));
				}
				else
					events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.NODAMAGECRIT, AttackSide.INITIATOR));
				int remaining2 = health2 - damageDealt;
				if(remaining2 <= 0)
					events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.DEFEATED, AttackSide.INITIATOR));
				paths.add(new RNGDivider2(this, (long) hitrate * critrate, 2, attackInfo, health1a,
						Math.max(0, remaining2), attacks1, attacks2, attacked1 + 1, attacked2, crits1 + 1,
						crits2, remaining2 <= 0, events3));
			}
		}
		else if(canAttack2)
		{
			//2
			List<SidedAttackAnalysisEvent> events2 = new ArrayList<>();
			int health2a = health2;
			if(attacked2 == 0 && attackInfo.calcT.healthCost > 0)
			{
				if(attackInfo.calcT.healthCost >= health2)
				{
					//cost too high
					paths.add(new RNGDivider2(this, 1, 0, attackInfo, health1,
							health2a, attacks1, 0, attacked1, 0, crits1, crits2, false, events2));
					return;
				}
				else
				{
					events2.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.HEALTHCOST, AttackSide.TARGET));
					health2a -= attackInfo.calcT.healthCost;
				}
			}
			events2.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.ATTACK, AttackSide.TARGET));
			int hitrate = attacked2 == 0 && attackInfo.calcT.autohit1 ? 100 : attackInfo.calcT.hitrate;
			int critrate = crits2 > 0 ? 0 : attackInfo.calcT.critrate;
			if(hitrate < 100)
			{
				//miss
				List<SidedAttackAnalysisEvent> events3 = new ArrayList<>(events2);
				events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.MISS, AttackSide.TARGET));
				paths.add(new RNGDivider2(this, 100 - hitrate, 1, attackInfo, health1,
						health2a, attacks1, attacks2, attacked1, attacked2 + 1, crits1, crits2, false, events3));
			}
			if(hitrate > 0 && critrate < 100)
			{
				//hit
				int damageDealt = attackInfo.calcT.melting1 && attacked2 == 0 ? attackInfo.calcT.meltDamage : attackInfo.calcT.damage;
				List<SidedAttackAnalysisEvent> events3 = new ArrayList<>(events2);
				if(damageDealt > 0)
				{
					if(attackInfo.calcT.melting1 && attacked2 == 0)
						events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.MELT, AttackSide.TARGET));
					else
						events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.HIT, AttackSide.TARGET));
				}
				else
					events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.NODAMAGE, AttackSide.TARGET));
				int remaining1 = health1 - damageDealt;
				if(remaining1 <= 0)
					events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.DEFEATED, AttackSide.TARGET));
				paths.add(new RNGDivider2(this, hitrate * (100L - critrate), 2, attackInfo, Math.max(0, remaining1),
						health2a, attacks1, attacks2, attacked1, attacked2 + 1, crits1, crits2, remaining1 <= 0, events3));
			}
			if(hitrate > 0 && critrate > 0)
			{
				//crit
				int damageDealt = attackInfo.calcT.melting1 && attacked2 == 0 ? attackInfo.calcT.meltCritDamage : attackInfo.calcT.critDamage;
				List<SidedAttackAnalysisEvent> events3 = new ArrayList<>(events2);
				if(damageDealt > 0)
				{
					if(attackInfo.calcT.melting1 && attacked2 == 0)
						events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.MELTCRIT, AttackSide.TARGET));
					else
						events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.CRIT, AttackSide.TARGET));
				}
				else
					events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.NODAMAGECRIT, AttackSide.TARGET));
				int remaining1 = health1 - damageDealt;
				if(remaining1 <= 0)
					events3.add(new SidedAttackAnalysisEvent(AttackAnalysisEvent.DEFEATED, AttackSide.TARGET));
				paths.add(new RNGDivider2(this, (long) hitrate * critrate, 2, attackInfo, Math.max(0, remaining1),
						health2a, attacks1, attacks2, attacked1, attacked2 + 1, crits1, crits2 + 1, remaining1 <= 0, events3));
			}
		}
	}

	@Override
	public RNGOutcome2 asOutcome()
	{
		List<SidedAttackAnalysisEvent> compareText = new ArrayList<>();
		compareText(compareText);
		return new RNGOutcome2(chanceC(), dividerC(), compareText.stream()
				.collect(Collectors.groupingBy(e -> e, Collectors.counting())).entrySet().stream()
				.map(e1 -> e1.getKey() + " " + e1.getValue()).sorted().collect(Collectors.toList()), attackInfo, health1, health2);
	}

	private void compareText(List<SidedAttackAnalysisEvent> list)
	{
		if(prev instanceof RNGDivider2 prev2)
			prev2.compareText(list);
		list.addAll(events);
	}
}