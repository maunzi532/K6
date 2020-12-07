package system;

import entity.*;
import text.*;

public class AllyCalc4
{
	private static final CharSequence[] EMPTY_TEXTS = new CharSequence[0];

	public final AllyInfo4 aI;
	public final int heal;
	public final CharSequence[] infos;

	public AllyCalc4(AllyInfo4 aI)
	{
		this.aI = aI;
		heal = aI.item().additional().getOrDefault("AbilityPower", 0)
				+ aI.character().systemChar().stat(Stats4.ABILITY_POWER);
		infos = new CharSequence[]
				{
						viewHealth(aI.character()),
						viewHealth(aI.target()),
						numToText(heal)
				};
	}

	private CharSequence viewHealth(XCharacter character)
	{
		return new ArgsText("attackinfo.health", character.currentHP(), character.maxHP());
	}

	private CharSequence numToText(int num)
	{
		return new ArgsText("i", num);
	}

	public CharSequence[] sideInfo1()
	{
		return new CharSequence[]
				{
						new ArgsText("allyinfo.heal", heal)
				};
	}

	public CharSequence[] sideInfo2()
	{
		return EMPTY_TEXTS;
	}

	public CharSequence[] sideInfoC()
	{
		return new CharSequence[]
				{
						new ArgsText("allyinfo.heal", heal)
				};
	}
}