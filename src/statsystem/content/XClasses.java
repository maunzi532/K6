package statsystem.content;

import java.util.*;
import statsystem.*;

public final class XClasses
{
	public static final XClasses INSTANCE = new XClasses();

	public final XClass[] xClasses;

	private XClasses()
	{
		xClasses = new XClass[13];
		xClasses[1] = new XClass(1, "class.bandit", 0, new int[]{9, 7, 6, 13, 5, 8, 5, 12}, 6,
				List.of(DaggerItem.INSTANCE, AxeItem.INSTANCE), List.of());
		xClasses[2] = new XClass(2, "class.soldier", 0, new int[]{11, 8, 10, 7, 6, 11, 7, 8}, 6,
				List.of(SpearItem.INSTANCE, CrossbowItem.INSTANCE), List.of());
		xClasses[3] = new XClass(3, "class.squire", 0, new int[]{7, 10, 11, 9, 10, 6, 9, 8}, 6,
				List.of(SpearItem.INSTANCE, MagicSwordItem.INSTANCE), List.of());
		xClasses[4] = new XClass(4, "class.hexer", 0, new int[]{6, 7, 12, 10, 9, 6, 11, 9}, 6,
				List.of(EnergySpellItem.INSTANCE), List.of());
		xClasses[5] = new XClass(5, "class.mage", 0, new int[]{7, 11, 9, 9, 11, 5, 8, 7}, 6,
				List.of(DarkSpellItem.INSTANCE, DaggerItem.INSTANCE), List.of());
		xClasses[6] = new XClass(6, "class.explorer", 0, new int[]{11, 6, 8, 9, 7, 10, 8, 10}, 6,
				List.of(AxeItem.INSTANCE, CrossbowItem.INSTANCE), List.of());
		xClasses[7] = new XClass(7, "class.gangster", 20, new int[]{7, 8, 10, 14, 5, 7, 6, 10}, 6,
				List.of(DaggerItem.INSTANCE, AxeItem.INSTANCE, GunItem.INSTANCE), List.of(XAbility.UNLIMITED_CRITICAL));
		xClasses[8] = new XClass(8, "class.general", 20, new int[]{12, 8, 9, 6, 8, 12, 9, 8}, 6,
				List.of(SpearItem.INSTANCE, CrossbowItem.INSTANCE, AxeItem.INSTANCE, MagicSwordItem.INSTANCE), List.of(XAbility.SHIELD_POWER));
		xClasses[9] = new XClass(9, "class.knight", 25, new int[]{9, 11, 12, 11, 9, 6, 10, 9}, 10,
				List.of(SpearItem.INSTANCE, MagicSwordItem.INSTANCE, LanceItem.INSTANCE), List.of(XAbility.TEAMSTRIKE));
		xClasses[10] = new XClass(10, "class.advhexer", 20, new int[]{7, 7, 14, 10, 8, 6, 13, 11}, 6,
				List.of(EnergySpellItem.INSTANCE, DarkSpellItem.INSTANCE), List.of(XAbility.LIFE_REFUND));
		xClasses[11] = new XClass(11, "class.archmage", 20, new int[]{6, 13, 9, 8, 12, 7, 8, 8}, 6,
				List.of(DarkSpellItem.INSTANCE, DaggerItem.INSTANCE, EnergySpellItem.INSTANCE, MagicSwordItem.INSTANCE), List.of(XAbility.CRITICAL_TARGET));
		xClasses[12] = new XClass(12, "class.captain", 20, new int[]{12, 7, 10, 9, 7, 9, 9, 11}, 6,
				List.of(AxeItem.INSTANCE, CrossbowItem.INSTANCE, GunItem.INSTANCE), List.of(XAbility.FAST_FIRE));
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

	public static XClass explorerClass()
	{
		return INSTANCE.xClasses[6];
	}
}