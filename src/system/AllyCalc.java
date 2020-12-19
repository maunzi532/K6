package system;

import entity.*;
import text.*;

public class AllyCalc
{
	private static final CharSequence[] EMPTY_TEXTS = new CharSequence[0];

	public final AllyInfo aI;
	public final int heal;
	public final CharSequence[] infos;

	public AllyCalc(AllyInfo aI)
	{
		this.aI = aI;
		heal = aI.item().additional().getOrDefault("AbilityPower", 0)
				+ aI.character().systemChar().stat(XStats.ABILITY_POWER);
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

	public void setSideInfo(SideInfoFrame side)
	{
		if(aI.character() == aI.target())
		{
			side.sidedInfo(aI.character(), e -> e.sideInfo(sideInfoC()));
		}
		else
		{
			side.sidedInfo(aI.character(), aI.character().sideInfo(sideInfo1()), aI.target(), aI.target().sideInfo(sideInfo2()));
		}
	}
}