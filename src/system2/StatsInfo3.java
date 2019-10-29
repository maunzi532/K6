package system2;

import java.util.*;
import java.util.stream.*;
import system2.content.*;

public class StatsInfo3
{
	private static final int[] DEFENDER_RANGES = new int[]{1, 2, 3, 4, 5, 6};
	private static final int PHYSICAL_ACCURACY_MULTIPLIER = 3;
	private static final int MAGICAL_ACCURACY_MULTIPLIER = 3;
	private static final int CRIT_MULTIPLIER = 2;

	public final Stats2 stats;
	public final AttackItem2 item;
	public final AM2Type mode;
	public final Set<Ability2> abilities;
	public final int[] ranges;
	public final int[] counter;
	public final boolean magical;
	public final int baseAttackCount;
	public final AdvantageType advType;
	private final int strength1;
	private final int weightDiff;
	private final AdaptiveType adaptiveType;
	private final int maxAdaptive;
	private final int adaptive;
	private final int tooHeavy;
	public final int healthCost;
	private final int exhausted;
	private final int finesse1;
	private final int skill1;
	private final int speed1;
	private final int luck1;
	private final int defense1;
	private final int evasion1;
	public final int attackPower;
	public final int finalSpeed;
	public final int accuracy;
	public final int crit;
	public final int defensePhysical;
	public final int defenseMagical;
	public final int evasionPhysical;
	public final int evasionMagical;
	public final int critProtection;

	public StatsInfo3(Stats2 stats, AttackMode2 mode1)
	{
		this.stats = stats;
		item = mode1.item;
		mode = mode1.type;
		List<ModifierAspect> modifiers = List.of(stats, item, mode);
		abilities = modifiers.stream().flatMap(e -> e.abilities().stream()).collect(Collectors.toSet());
		ranges = abilities.contains(Ability2.DEFENDER) ? DEFENDER_RANGES : item.getRanges(false);
		counter = abilities.contains(Ability2.DEFENDER) ? DEFENDER_RANGES : item.getRanges(true);
		magical = item.magical() != mode.inverseMagical();
		baseAttackCount = mode.attackCount() + (abilities.contains(Ability2.FAST) ? 1 : 0);
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
}