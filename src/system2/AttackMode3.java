package system2;

import entity.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.image.*;
import system2.content.*;

public class AttackMode3 implements XMode
{
	private static final int[] NO_RANGES = new int[]{};
	private static final int[] DEFENDER_RANGES = new int[]{1, 2, 3, 4, 5, 6};
	private static final int PHYSICAL_ACCURACY_MULTIPLIER = 3;
	private static final int MAGICAL_ACCURACY_MULTIPLIER = 3;
	private static final int CRIT_MULTIPLIER = 2;

	public final Stats2 stats;
	public final AttackItem2 item;
	public final AM2Type mode;
	public final Set<Ability2> abilities;
	private final List<AbilityText> abilityTexts;
	public final int[] ranges;
	public final int[] counter;
	public final boolean magical;
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

	public AttackMode3(Stats2 stats, AttackMode4 mode4)
	{
		this.stats = stats;
		item = mode4.item;
		mode = mode4.mode;
		List<ModifierAspect> modifiers = List.of(stats, item, mode);
		abilities = modifiers.stream().flatMap(e -> e.abilities().stream()).collect(Collectors.toSet());
		abilityTexts = modifiers.stream().flatMap(e -> e.abilities().stream().map(f -> new AbilityText(e, f))).collect(Collectors.toList());
		ranges = abilities.contains(Ability2.DEFENDER) ? DEFENDER_RANGES : item.getRanges(false);
		counter = abilities.contains(Ability2.DEFENDER) ? DEFENDER_RANGES : item.getRanges(true);
		magical = item.magical() != mode.inverseMagical();
		attackCount = mode.attackCount() + (abilities.contains(Ability2.FAST) ? 1 : 0);
		advType = item.getAdvType();
		exhausted = stats.getExhaustion();
		strength1 = stats.getStrength() - exhausted;
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
		finesse1 = stats.getFinesse() + (adaptiveType == AdaptiveType.FINESSE ? adaptive : 0) - exhausted;
		skill1 = stats.getSkill() + (adaptiveType == AdaptiveType.SKILL ? adaptive : 0) - exhausted;
		speed1 = stats.getSpeed() + (adaptiveType == AdaptiveType.SPEED ? adaptive : 0) - exhausted;
		luck1 = stats.getLuck() + (adaptiveType == AdaptiveType.LUCK ? adaptive : 0);
		defense1 = stats.getDefense() + (adaptiveType == AdaptiveType.DEFENSE ? adaptive : 0) - exhausted;
		evasion1 = stats.getEvasion() + (adaptiveType == AdaptiveType.EVASION ? adaptive : 0) - exhausted;
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
		if(magical)
		{
			attackPower1 += skill1;
			accuracy1 += finesse1 * PHYSICAL_ACCURACY_MULTIPLIER;
		}
		else
		{
			attackPower1 += finesse1;
			accuracy1 += skill1 * PHYSICAL_ACCURACY_MULTIPLIER;
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

	public AttackMode3(Stats2 stats)
	{
		this.stats = stats;
		item = null;
		mode = null;
		List<ModifierAspect> modifiers = List.of(stats);
		abilities = modifiers.stream().flatMap(e -> e.abilities().stream()).collect(Collectors.toSet());
		abilityTexts = modifiers.stream().flatMap(e -> e.abilities().stream().map(f -> new AbilityText(e, f))).collect(Collectors.toList());
		ranges = NO_RANGES;
		counter = NO_RANGES;
		magical = false;
		attackCount = 0;
		advType = AdvantageType.DEFEND;
		exhausted = stats.getExhaustion();
		strength1 = stats.getStrength() - exhausted;
		weightDiff = 0;
		adaptiveType = AdaptiveType.NONE;
		maxAdaptive = 0;
		adaptive = 0;
		tooHeavy = 0;
		healthCost = 0;
		finesse1 = stats.getFinesse() - exhausted;
		skill1 = stats.getSkill() - exhausted;
		speed1 = stats.getSpeed() - exhausted;
		luck1 = stats.getLuck();
		defense1 = stats.getDefense() - exhausted;
		evasion1 = stats.getEvasion() - exhausted;
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

	public int defense(boolean magical)
	{
		return magical ? defenseMagical : defensePhysical;
	}

	public int evasion(boolean magical)
	{
		return magical ? evasionMagical : defenseMagical;
	}

	@Override
	public XMode shortVersion()
	{
		return item == null || mode == null ? AttackMode4.EVADE_MODE : new AttackMode4(item, mode);
	}

	@Override
	public Image image()
	{
		return item != null ? item.image() : null;
	}

	@Override
	public String tile()
	{
		return mode != null ? mode.tile() : "Evade";
	}

	@Override
	public List<String> modeInfo()
	{
		return mode.info();
	}

	public List<String> info()
	{
		List<String> list = new ArrayList<>();
		if(attackCount > 0)
		{
			list.add((magical ? "Magical\n" : "Physical\n") + attackCount + " attacks");
		}
		else
		{
			list.add("");
		}
		list.add("Range\n" + AttackItem2.displayRange(ranges));
		list.add("Counter\n" + AttackItem2.displayRange(counter));
		list.add("Adv. Type\n" + advType.name);
		if(adaptiveType == AdaptiveType.COST)
		{
			list.add("Cast with\n" + healthCost + " health");
		}
		else
		{
			list.add("");
		}
		if(attackCount > 0)
		{
			list.add("Attack\n" + attackPower);
		}
		list.add("Speed\n" + finalSpeed);
		if(attackCount > 0)
		{
			list.add("Acc%\n" + accuracy);
			list.add("Crit%\n" + crit);
		}
		list.add("Def (phy)\n" + defensePhysical);
		list.add("Def (mag)\n" + defenseMagical);
		list.add("Evade (phy)\n" + evasionPhysical);
		list.add("Evade (mag)\n" + evasionMagical);
		list.add("Prevent Crit\n" + critProtection);
		abilityTexts.forEach(e -> list.add(e.text()));
		return list;
	}

	public static AttackMode3 convert(Stats2 stats, AttackMode4 mode4)
	{
		if(mode4.active)
			return new AttackMode3(stats, mode4);
		else
			return new AttackMode3(stats);
	}
}