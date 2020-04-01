package statsystem;

import java.util.*;

public final class XClass
{
	public final int code;
	public final String className;
	public final StaticLevelSystem levelSystem;
	public final int movement;
	public final List<AI2Class> usableItems;
	public final List<XAbility> abilities;

	public XClass(int code, String className, int baseLevel, int[] baseStats, int[] increase, int movement,
			List<AI2Class> usableItems, List<XAbility> abilities)
	{
		this.code = code;
		this.className = className;
		levelSystem = new StaticLevelSystem(baseLevel, baseStats, increase);
		this.movement = movement;
		this.usableItems = usableItems;
		this.abilities = abilities;
	}

	public XClass(int code, String className, int baseLevel, int[] baseStats, int movement,
			List<AI2Class> usableItems, List<XAbility> abilities)
	{
		this.code = code;
		this.className = className;
		int[] increase = new int[baseStats.length];
		for(int i = 0; i < baseStats.length; i++)
		{
			increase[i] = baseStats[i] * 5;
		}
		levelSystem = new StaticLevelSystem(baseLevel, baseStats, increase);
		this.movement = movement;
		this.usableItems = usableItems;
		this.abilities = abilities;
	}

	public int getStat(int num, int level)
	{
		return levelSystem.forLevel(num, level);
	}
}