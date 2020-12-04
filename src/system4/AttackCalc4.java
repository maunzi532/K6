package system4;

import entity.*;
import text.*;

public class AttackCalc4
{
	private static final CharSequence[] EMPTY_TEXTS = new CharSequence[0];

	public final AttackInfo4 aI;
	public final boolean canAttack1;
	public final boolean canAttack2;
	public final int attackCount1;
	public final int attackCount2;
	public final int damage1;
	public final int damage2;
	public final int accuracy1;
	public final int accuracy2;
	public final CharSequence[] infos;

	public AttackCalc4(AttackInfo4 aI)
	{
		this.aI = aI;
		attackCount1 = aI.initiatorItem().additional().getOrDefault("AttackCount", 1);
		attackCount2 = aI.targetItem().additional().getOrDefault("AttackCount", 1);
		if(aI.initiatorItem().abilityRanges() != null)
			canAttack1 = attackCount1 > 0 && aI.initiatorItem().abilityRanges()
					.hasRange(aI.distance(), aI.initiator().systemChar().stat(Stats4.ABILITY_RANGE));
		else
			canAttack1 = attackCount1 > 0 && aI.initiatorItem().attackRanges().hasRange(aI.distance(), 0);
		canAttack2 = attackCount2 > 0 && aI.targetItem().defendRanges().hasRange(aI.distance(), 0);
		int attack1;
		if(aI.initiatorItem().abilityRanges() != null)
			attack1 = aI.initiatorItem().additional().getOrDefault("AbilityPower", 0)
					+ aI.initiator().systemChar().stat(Stats4.ABILITY_POWER);
		else
			attack1 = aI.initiatorItem().additional().getOrDefault("AttackPower", 0)
					+ aI.initiator().systemChar().stat(Stats4.ATTACK);
		int attack2 = aI.targetItem().additional().getOrDefault("AttackPower", 0)
				+ aI.target().systemChar().stat(Stats4.ATTACK);
		int defense1;
		if(aI.targetItem().additional().getOrDefault("Magic", 0) > 0)
			defense1 = 0;
		else
			defense1 = aI.initiator().systemChar().stat(Stats4.DEFENSE);
		int defense2;
		if(aI.initiatorItem().additional().getOrDefault("Magic", 0) > 0)
			defense2 = 0;
		else
			defense2 = aI.target().systemChar().stat(Stats4.DEFENSE);
		damage1 = Math.max(0, attack1 - defense2);
		damage2 = Math.max(0, attack2 - defense1);
		int baseAcc1 = aI.initiatorItem().additional().getOrDefault("Accuracy", 0)
				+ aI.initiator().systemChar().stat(Stats4.ACCURACY);
		int baseAcc2 = aI.targetItem().additional().getOrDefault("Accuracy", 0)
				+ aI.target().systemChar().stat(Stats4.ACCURACY);
		int dodge1 = aI.initiator().systemChar().stat(Stats4.DODGE);
		int dodge2 = aI.target().systemChar().stat(Stats4.DODGE);
		accuracy1 = baseAcc1 - dodge2;
		accuracy2 = baseAcc2 - dodge1;

		infos = new CharSequence[]
				{
						viewHealth(aI.initiator()),
						viewHealth(aI.target()),
						numToText(canAttack1, attackCount1),
						numToText(canAttack2, attackCount2),
						numToText(canAttack1, damage1),
						numToText(canAttack2, damage2),
						numToText(canAttack1, accuracy1),
						numToText(canAttack2, accuracy2),
				};
	}

	private CharSequence viewHealth(XCharacter character)
	{
		return new ArgsText("attackinfo.health", character.currentHP(), character.maxHP());
	}

	private CharSequence numToText(boolean canAttack, int num)
	{
		return canAttack ? String.valueOf(num) : "";
	}

	public CharSequence[] sideInfo1()
	{
		return canAttack1 ? new CharSequence[]
				{
						String.valueOf(damage1),
						String.valueOf(attackCount1),
						String.valueOf(accuracy1)
				} : EMPTY_TEXTS;
	}

	public CharSequence[] sideInfo2()
	{
		return canAttack2 ? new CharSequence[]
				{
						String.valueOf(damage2),
						String.valueOf(attackCount2),
						String.valueOf(accuracy2)
				} : EMPTY_TEXTS;
	}

	@Override
	public String toString()
	{
		return "AttackCalc4{" +
				"canAttack1=" + canAttack1 +
				", canAttack2=" + canAttack2 +
				", attackCount1=" + attackCount1 +
				", attackCount2=" + attackCount2 +
				", damage1=" + damage1 +
				", damage2=" + damage2 +
				", accuracy1=" + accuracy1 +
				", accuracy2=" + accuracy2 +
				'}';
	}
}