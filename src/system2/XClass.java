package system2;

import java.util.*;
import system2.content.*;

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

	public static XClass banditClass()
	{
		return new XClass(1, "Bandit", new int[]{9, 7, 6, 13, 5, 8, 5, 60}, 6, List.of(DaggerItem.class, AxeItem.class), List.of());
	}

	public static XClass soldierClass()
	{
		return new XClass(2, "Soldier", new int[]{11, 8, 10, 7, 6, 11, 7, 40}, 6, List.of(SpearItem.class, CrossbowItem.class), List.of());
	}

	public static XClass squireClass()
	{
		return new XClass(3, "Squire", new int[]{7, 10, 11, 9, 10, 6, 9, 40}, 6, List.of(SpearItem.class, DaggerItem.class), List.of());
	}

	public static XClass hexerClass()
	{
		return new XClass(4, "Hexer", new int[]{6, 7, 12, 10, 9, 6, 11, 45}, 6, List.of(SpellItem.class), List.of());
	}

	public static XClass mageClass()
	{
		return new XClass(5, "Mage", new int[]{7, 11, 9, 9, 11, 5, 8, 35}, 6, List.of(SpellItem.class, DaggerItem.class), List.of());
	}

	public static XClass pirateClass()
	{
		return new XClass(6, "Pirate", new int[]{11, 6, 8, 9, 7, 10, 8, 50}, 6, List.of(AxeItem.class, CrossbowItem.class), List.of());
	}
}