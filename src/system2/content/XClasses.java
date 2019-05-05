package system2.content;

import java.util.*;
import system2.*;

public class XClasses
{
	public static final XClasses INSTANCE = new XClasses();

	public final XClass[] xClasses;

	public XClasses()
	{
		xClasses = new XClass[7];
		xClasses[1] = new XClass(1, "Bandit", new int[]{9, 7, 6, 13, 5, 8, 5, 60}, 6, List.of(DaggerItem.class, AxeItem.class), List.of());
		xClasses[2] = new XClass(2, "Soldier", new int[]{11, 8, 10, 7, 6, 11, 7, 40}, 6, List.of(SpearItem.class, CrossbowItem.class), List.of());
		xClasses[3] = new XClass(3, "Squire", new int[]{7, 10, 11, 9, 10, 6, 9, 40}, 6, List.of(SpearItem.class, DaggerItem.class), List.of());
		xClasses[4] = new XClass(4, "Hexer", new int[]{6, 7, 12, 10, 9, 6, 11, 45}, 6, List.of(SpellItem.class), List.of());
		xClasses[5] = new XClass(5, "Mage", new int[]{7, 11, 9, 9, 11, 5, 8, 35}, 6, List.of(SpellItem.class, DaggerItem.class), List.of());
		xClasses[6] = new XClass(6, "Pirate", new int[]{11, 6, 8, 9, 7, 10, 8, 50}, 6, List.of(AxeItem.class, CrossbowItem.class), List.of());
	}

	public static XClass banditClass()
	{
		return INSTANCE.xClasses[1];
	}

	public static XClass soldierClass()
	{
		return INSTANCE.xClasses[2];
	}

	public static XClass squireClass()
	{
		return INSTANCE.xClasses[3];
	}

	public static XClass hexerClass()
	{
		return INSTANCE.xClasses[4];
	}

	public static XClass mageClass()
	{
		return INSTANCE.xClasses[5];
	}

	public static XClass pirateClass()
	{
		return INSTANCE.xClasses[6];
	}
}