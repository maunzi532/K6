package system2;

import java.util.*;

public class XClass
{
	public final int code;
	public final String className;
	public final int[] baseStats;
	public final int[] increase;
	public final int movement;
	public final List<Class> usableItems;
	public final List<Ability2> abilities;

	public XClass(int code, String className, int[] baseStats, int[] increase, int movement,
			List<Class> usableItems, List<Ability2> abilities)
	{
		this.code = code;
		this.className = className;
		this.baseStats = baseStats;
		this.increase = increase;
		this.movement = movement;
		this.usableItems = usableItems;
		this.abilities = abilities;
	}

	public XClass(int code, String className, int[] baseStats, int movement,
			List<Class> usableItems, List<Ability2> abilities)
	{
		this.code = code;
		this.className = className;
		this.baseStats = baseStats;
		increase = new int[baseStats.length];
		for(int i = 0; i < baseStats.length; i++)
		{
			increase[i] = baseStats[i] * 5;
		}
		this.movement = movement;
		this.usableItems = usableItems;
		this.abilities = abilities;
	}

	public int getStat(int num, int level)
	{
		return baseStats[num] + (increase[num] * level) / 100;
	}
}