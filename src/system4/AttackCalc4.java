package system4;

import entity.*;
import text.*;

public class AttackCalc4
{
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
		if(aI.abilityAttack())
			canAttack1 = attackCount1 > 0 && aI.initiatorItem().attackRanges()
					.hasRange(aI.distance(), aI.initiator().systemChar().stat(Stats4.ABILITY_RANGE));
		else
			canAttack1 = attackCount1 > 0 && aI.initiatorItem().attackRanges().hasRange(aI.distance(), 0);
		canAttack2 = attackCount2 > 0 && aI.targetItem().defendRanges().hasRange(aI.distance(), 0);
		int attack1;
		if(aI.abilityAttack())
			attack1 = aI.initiatorItem().additional().getOrDefault("AttackPower", 0)
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
}