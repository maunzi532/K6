package system2.analysis;

import entity.analysis.*;
import java.util.*;
import java.util.stream.*;
import system2.*;

public class RNGDivider2 extends RNGDivider
{
	private AttackInfo2 aI;
	private int health1;
	private int health2;
	private int attacks1;
	private int attacks2;
	private int attacked1;
	private int attacked2;
	private int crits1;
	private int crits2;
	private boolean end;
	private List<String> events;

	public RNGDivider2(AttackInfo2 aI)
	{
		super(null, 1, 0);
		this.aI = aI;
		health1 = aI.stats.getCurrentHealth();
		health2 = aI.statsT.getCurrentHealth();
		attacks1 = aI.calc.attackCount;
		attacks2 = aI.calcT.attackCount;
		attacked1 = 0;
		attacked2 = 0;
		crits1 = 0;
		crits2 = 0;
		events = List.of();
	}

	public RNGDivider2(RNGDivider prev, long chance, long divider, AttackInfo2 aI, int health1, int health2,
			int attacks1, int attacks2, int attacked1, int attacked2, int crits1, int crits2, boolean end,
			List<String> events)
	{
		super(prev, chance, divider);
		this.aI = aI;
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

	public List<String> getEvents()
	{
		return events;
	}

	public AttackInfo2 getAttackInfo()
	{
		return aI;
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
			List<String> events2 = new ArrayList<>();
			int health1a = health1;
			if(attacked1 == 0 && aI.calc.cost > 0)
			{
				if(aI.calc.cost >= health1)
				{
					//cost too high
					paths.add(new RNGDivider2(this, 1, 0, aI, health1a,
							health2, 0, attacks2, 0, attacked2, crits1, crits2, false, events2));
					return;
				}
				else
				{
					events2.add("healthcost1");
					health1a -= aI.calc.cost;
				}
			}
			events2.add("attack1");
			int hitrate = attacked1 == 0 && aI.calc.autohit1 ? 100 : aI.calc.hitrate;
			int critrate = crits1 > 0 ? 0 : aI.calc.critrate;
			if(hitrate < 100)
			{
				//miss
				List<String> events3 = new ArrayList<>(events2);
				events3.add("miss1");
				paths.add(new RNGDivider2(this, 100 - hitrate, 1, aI, health1a,
						health2, attacks1, attacks2, attacked1 + 1, attacked2, crits1, crits2, false, events3));
			}
			if(hitrate > 0 && critrate < 100)
			{
				//hit
				int damageDealt = aI.calc.melting1 ? aI.calc.meltDamage : aI.calc.damage;
				List<String> events3 = new ArrayList<>(events2);
				if(damageDealt > 0)
				{
					if(aI.calc.melting1 && attacked1 == 0)
						events3.add("melt1");
					else
						events3.add("hit1");
				}
				else
					events3.add("nodamage1");
				int remaining2 = health2 - damageDealt;
				if(remaining2 <= 0)
					events3.add("defeated1");
				paths.add(new RNGDivider2(this, hitrate * (100 - critrate), 2, aI, health1a,
						Math.max(0, remaining2), attacks1, attacks2, attacked1 + 1, attacked2, crits1,
						crits2, remaining2 <= 0, events3));
			}
			if(hitrate > 0 && critrate > 0)
			{
				//crit
				int damageDealt = aI.calc.melting1 ? aI.calc.meltCritDamage : aI.calc.critDamage;
				List<String> events3 = new ArrayList<>(events2);
				if(damageDealt > 0)
				{
					if(aI.calc.melting1 && attacked1 == 0)
						events3.add("meltcrit1");
					else
						events3.add("crit1");
				}
				else
					events3.add("nodamagecrit1");
				int remaining2 = health2 - damageDealt;
				if(remaining2 <= 0)
					events3.add("defeated1");
				paths.add(new RNGDivider2(this, hitrate * critrate, 2, aI, health1a,
						Math.max(0, remaining2), attacks1, attacks2, attacked1 + 1, attacked2, crits1 + 1,
						crits2, remaining2 <= 0, events3));
			}
		}
		else if(canAttack2)
		{
			//2
			List<String> events2 = new ArrayList<>();
			int health2a = health2;
			if(attacked2 == 0 && aI.calcT.cost > 0)
			{
				if(aI.calcT.cost >= health2)
				{
					//cost too high
					paths.add(new RNGDivider2(this, 1, 0, aI, health1,
							health2a, attacks1, 0, attacked1, 0, crits1, crits2, false, events2));
					return;
				}
				else
				{
					events2.add("healthcost2");
					health2a -= aI.calcT.cost;
				}
			}
			events2.add("attack2");
			int hitrate = attacked2 == 0 && aI.calcT.autohit1 ? 100 : aI.calcT.hitrate;
			int critrate = crits2 > 0 ? 0 : aI.calcT.critrate;
			if(hitrate < 100)
			{
				//miss
				List<String> events3 = new ArrayList<>(events2);
				events3.add("miss2");
				paths.add(new RNGDivider2(this, 100 - hitrate, 1, aI, health1,
						health2a, attacks1, attacks2, attacked1, attacked2 + 1, crits1, crits2, false, events3));
			}
			if(hitrate > 0 && critrate < 100)
			{
				//hit
				int damageDealt = aI.calcT.melting1 ? aI.calcT.meltDamage : aI.calcT.damage;
				List<String> events3 = new ArrayList<>(events2);
				if(damageDealt > 0)
				{
					if(aI.calcT.melting1 && attacked2 == 0)
						events3.add("melt2");
					else
						events3.add("hit2");
				}
				else
					events3.add("nodamage2");
				int remaining1 = health1 - damageDealt;
				if(remaining1 <= 0)
					events3.add("defeated2");
				paths.add(new RNGDivider2(this, hitrate * (100 - critrate), 2, aI, Math.max(0, remaining1),
						health2a, attacks1, attacks2, attacked1, attacked2 + 1, crits1, crits2, remaining1 <= 0, events3));
			}
			if(hitrate > 0 && critrate > 0)
			{
				//crit
				int damageDealt = aI.calcT.melting1 ? aI.calcT.meltCritDamage : aI.calcT.critDamage;
				List<String> events3 = new ArrayList<>(events2);
				if(damageDealt > 0)
				{
					if(aI.calcT.melting1 && attacked2 == 0)
						events3.add("meltcrit2");
					else
						events3.add("crit2");
				}
				else
					events3.add("nodamagecrit2");
				int remaining1 = health1 - damageDealt;
				if(remaining1 <= 0)
					events3.add("defeated2");
				paths.add(new RNGDivider2(this, hitrate * critrate, 2, aI, Math.max(0, remaining1),
						health2a, attacks1, attacks2, attacked1, attacked2 + 1, crits1, crits2 + 1, remaining1 <= 0, events3));
			}
		}
	}

	@Override
	public RNGOutcome asOutcome()
	{
		List<String> compareText = new ArrayList<>();
		compareText(compareText);
		return new RNGOutcome2(chanceC(), dividerC(), compareText.stream()
				.collect(Collectors.groupingBy(e -> e, Collectors.counting())).entrySet().stream()
				.map(f -> f.getKey() + " " + f.getValue()).sorted().collect(Collectors.toList()), aI, health1, health2);
	}

	private void compareText(List<String> list)
	{
		if(prev instanceof RNGDivider2)
			((RNGDivider2) prev).compareText(list);
		list.addAll(events);
	}
}