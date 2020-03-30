package statsystem;

import java.util.*;
import java.util.stream.*;
import statsystem.content.*;

public final class AttackMode3
{
	private static final int[] NO_RANGES = {};
	private static final int[] DEFENDER_RANGES = {1, 2, 3, 4, 5, 6};
	private static final int PHYSICAL_ACCURACY_MULTIPLIER = 3;
	private static final int MAGICAL_ACCURACY_MULTIPLIER = 3;
	private static final int CRIT_MULTIPLIER = 2;

	public final Stats stats;
	public final AttackItem item;
	public final AM2Type mode;
	public final Set<Ability2> abilities;
	private final List<AbilityText> abilityTexts;
	public final int[] ranges;
	public final int[] counter;
	public final DefenseType defenseType;
	public final int attackCount;
	public final AdvantageType advType;
	private final int strength1;
	private final int weightDiff;
	private final AdaptiveType adaptiveType;
	private final int maxAdaptive;
	private final int adaptive;
	private final int tooHeavy;
	private final int exhausted;
	private final int finesse1;
	private final int skill1;
	private final int speed1;
	private final int luck1;
	private final int defense1;
	private final int evasion1;
	public final int healthCost;
	public final int attackPower;
	public final int finalSpeed;
	public final int accuracy;
	public final int crit;
	public final int defensePhysical;
	public final int defenseMagical;
	public final int evasionPhysical;
	public final int evasionMagical;
	public final int critProtection;

	private AttackMode3(Stats stats, AttackMode mode4)
	{
		this.stats = stats;
		item = mode4.item;
		mode = mode4.mode;
		List<ModifierAspect> modifiers = List.of(stats, item, mode);
		abilities = modifiers.stream().flatMap(modifier -> modifier.abilities().stream()).collect(Collectors.toSet());
		abilityTexts = modifiers.stream().flatMap(modifier -> modifier.abilities()
				.stream().map(ability -> new AbilityText(modifier, ability))).collect(Collectors.toList());
		ranges = abilities.contains(Ability2.DEFENDER) ? DEFENDER_RANGES : item.getRanges(AttackSide.INITIATOR);
		counter = abilities.contains(Ability2.DEFENDER) ? DEFENDER_RANGES : item.getRanges(AttackSide.TARGET);
		defenseType = mode.inverseDefenseType() ? DefenseType.inverted(item.defenseType()) : item.defenseType();
		attackCount = mode.attackCount() + (abilities.contains(Ability2.FAST) ? 1 : 0);
		advType = item.getAdvantageType();
		exhausted = stats.exhaustion();
		strength1 = stats.strength() - exhausted;
		weightDiff = strength1 - modifiers.stream().mapToInt(ModifierAspect::heavy).sum();
		adaptiveType = item.getAdaptiveType();
		maxAdaptive = item.getAdaptive();
		if(adaptiveType == AdaptiveType.COST)
		{
			adaptive = 0;
			tooHeavy = Math.max(0, -weightDiff - maxAdaptive);
			healthCost = Math.max(0, Math.min(-weightDiff, maxAdaptive));
		}
		else
		{
			adaptive = Math.max(0, Math.min(maxAdaptive, weightDiff));
			tooHeavy = Math.max(0, -weightDiff);
			healthCost = 0;
		}
		finesse1 = stats.finesse() + (adaptiveType == AdaptiveType.FINESSE ? adaptive : 0) - exhausted;
		skill1 = stats.skill() + (adaptiveType == AdaptiveType.SKILL ? adaptive : 0) - exhausted;
		speed1 = stats.speed() + (adaptiveType == AdaptiveType.SPEED ? adaptive : 0) - exhausted;
		luck1 = stats.luck() + (adaptiveType == AdaptiveType.LUCK ? adaptive : 0);
		defense1 = stats.defense() + (adaptiveType == AdaptiveType.DEFENSE ? adaptive : 0) - exhausted;
		evasion1 = stats.evasion() + (adaptiveType == AdaptiveType.EVASION ? adaptive : 0) - exhausted;
		int attackPower1 = modifiers.stream().mapToInt(ModifierAspect::attackPower).sum();
		int speedMod1 = modifiers.stream().mapToInt(ModifierAspect::speedMod).sum();
		int accuracy1 = modifiers.stream().mapToInt(ModifierAspect::accuracy).sum();
		int crit1 = modifiers.stream().mapToInt(ModifierAspect::crit).sum();
		int defensePhysical1 = modifiers.stream().mapToInt(ModifierAspect::defensePhysical).sum();
		int defenseMagical1 = modifiers.stream().mapToInt(ModifierAspect::defenseMagical).sum();
		int evasionPhysical1 = modifiers.stream().mapToInt(ModifierAspect::evasionPhysical).sum();
		int evasionMagical1 = modifiers.stream().mapToInt(ModifierAspect::evasionMagical).sum();
		int critProtection1 = modifiers.stream().mapToInt(ModifierAspect::critProtection).sum();
		attackPower1 -= tooHeavy;
		speedMod1 -= tooHeavy;
		switch(defenseType)
		{
			case PHYSICAL ->
					{
						attackPower1 += finesse1;
						accuracy1 += skill1 * PHYSICAL_ACCURACY_MULTIPLIER;
					}
			case MAGICAL ->
					{
						attackPower1 += skill1;
						accuracy1 += finesse1 * PHYSICAL_ACCURACY_MULTIPLIER;
					}
		}
		speedMod1 += speed1;
		crit1 += luck1 * CRIT_MULTIPLIER;
		defensePhysical1 += defense1;
		defenseMagical1 += evasion1;
		evasionPhysical1 += evasion1 * PHYSICAL_ACCURACY_MULTIPLIER;
		evasionMagical1 += luck1 * MAGICAL_ACCURACY_MULTIPLIER;
		critProtection1 += luck1 * CRIT_MULTIPLIER;
		attackPower = attackPower1;
		finalSpeed = speedMod1;
		accuracy = accuracy1;
		crit = crit1;
		defensePhysical = defensePhysical1;
		defenseMagical = defenseMagical1;
		evasionPhysical = evasionPhysical1;
		evasionMagical = evasionMagical1;
		critProtection = critProtection1;
	}

	private AttackMode3(Stats stats)
	{
		this.stats = stats;
		item = null;
		mode = EvadeMode.INSTANCE;
		List<ModifierAspect> modifiers = List.of(stats, mode);
		abilities = modifiers.stream().flatMap(modifier -> modifier.abilities().stream()).collect(Collectors.toSet());
		abilityTexts = modifiers.stream().flatMap(modifier -> modifier.abilities()
				.stream().map(ability -> new AbilityText(modifier, ability))).collect(Collectors.toList());
		ranges = NO_RANGES;
		counter = NO_RANGES;
		defenseType = DefenseType.NONE;
		attackCount = 0;
		advType = AdvantageType.DEFEND;
		exhausted = stats.exhaustion();
		strength1 = stats.strength() - exhausted;
		weightDiff = 0;
		adaptiveType = AdaptiveType.NONE;
		maxAdaptive = 0;
		adaptive = 0;
		tooHeavy = 0;
		healthCost = 0;
		finesse1 = stats.finesse() - exhausted;
		skill1 = stats.skill() - exhausted;
		speed1 = stats.speed() - exhausted;
		luck1 = stats.luck();
		defense1 = stats.defense() - exhausted;
		evasion1 = stats.evasion() - exhausted;
		int speedMod1 = modifiers.stream().mapToInt(ModifierAspect::speedMod).sum();
		int defensePhysical1 = modifiers.stream().mapToInt(ModifierAspect::defensePhysical).sum();
		int defenseMagical1 = modifiers.stream().mapToInt(ModifierAspect::defenseMagical).sum();
		int evasionPhysical1 = modifiers.stream().mapToInt(ModifierAspect::evasionPhysical).sum();
		int evasionMagical1 = modifiers.stream().mapToInt(ModifierAspect::evasionMagical).sum();
		int critProtection1 = modifiers.stream().mapToInt(ModifierAspect::critProtection).sum();
		speedMod1 += speed1;
		defensePhysical1 += defense1;
		defenseMagical1 += evasion1;
		evasionPhysical1 += evasion1 * PHYSICAL_ACCURACY_MULTIPLIER;
		evasionMagical1 += luck1 * MAGICAL_ACCURACY_MULTIPLIER;
		critProtection1 += luck1 * CRIT_MULTIPLIER;
		attackPower = 0;
		finalSpeed = speedMod1;
		accuracy = 0;
		crit = 0;
		defensePhysical = defensePhysical1;
		defenseMagical = defenseMagical1;
		evasionPhysical = evasionPhysical1;
		evasionMagical = evasionMagical1;
		critProtection = critProtection1;
	}

	public int defense(DefenseType against)
	{
		return switch(against)
				{
					case PHYSICAL -> defensePhysical;
					case MAGICAL -> defenseMagical;
					case NONE -> 0;
				};
	}

	public int evasion(DefenseType against)
	{
		return switch(against)
				{
					case PHYSICAL -> evasionPhysical;
					case MAGICAL -> evasionMagical;
					case NONE -> 0;
				};
	}

	public int[] ranges(AttackSide side)
	{
		return switch(side)
				{
					case INITIATOR -> ranges;
					case TARGET -> counter;
				};
	}

	public AttackMode shortVersion()
	{
		return item == null || mode == null ? AttackMode.EVADE_MODE : new AttackMode(item, mode);
	}

	public String tile()
	{
		return mode != null ? mode.tile() : "Evade";
	}

	public List<String> modeInfo()
	{
		return mode.info();
	}

	public List<String> info()
	{
		List<String> list = new ArrayList<>();
		if(attackCount > 0)
		{
			list.add(attackCount + " attacks\n" + defenseType.text);
		}
		else
		{
			list.add("");
		}
		list.add("Range\n" + AttackItem.displayRange(ranges));
		list.add("Counter\n" + AttackItem.displayRange(counter));
		list.add("Adv. Type\n" + advType.name);
		if(attackCount > 0)
		{
			if(adaptiveType == AdaptiveType.COST)
			{
				list.add("Cast with\n" + healthCost + " health");
			}
			else
			{
				list.add("");
			}
			list.add("Attack\n" + attackPower);
			list.add("Acc%\n" + accuracy);
			list.add("Crit%\n" + crit);
		}
		list.add("Speed\n" + finalSpeed);
		list.add("Def (phy)\n" + defensePhysical);
		list.add("Def (mag)\n" + defenseMagical);
		list.add("Evade (phy)\n" + evasionPhysical);
		list.add("Evade (mag)\n" + evasionMagical);
		list.add("Prevent Crit\n" + critProtection);
		abilityTexts.forEach(e -> list.add(e.text()));
		return list;
	}

	public static AttackMode3 convert(Stats stats, AttackMode mode4)
	{
		if(mode4.active)
			return new AttackMode3(stats, mode4);
		else
			return new AttackMode3(stats);
	}
}