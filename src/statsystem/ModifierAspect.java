package statsystem;

import java.util.*;
import text.*;

public interface ModifierAspect
{
	String nameForAbility();

	List<Ability2> abilities();

	default int heavy()
	{
		return 0;
	}

	default int attackPower()
	{
		return 0;
	}

	default int speedMod()
	{
		return 0;
	}

	default int accuracy()
	{
		return 0;
	}

	default int crit()
	{
		return 0;
	}

	default	int defensePhysical()
	{
		return 0;
	}

	default	int defenseMagical()
	{
		return 0;
	}

	default	int evasionPhysical()
	{
		return 0;
	}

	default	int evasionMagical()
	{
		return 0;
	}

	default	int critProtection()
	{
		return 0;
	}

	default List<? extends CharSequence> detailedInfo(boolean p)
	{
		String pKey = p ? "i.plus" : "i";
		List<CharSequence> list = new ArrayList<>();
		add(list, "modifier.weight", pKey, heavy());
		add(list, "modifier.attackpower", pKey, attackPower());
		add(list, "modifier.speed", pKey, speedMod());
		add(list, "modifier.accuracy", pKey, accuracy());
		add(list, "modifier.crit", pKey, crit());
		if(defensePhysical() == defenseMagical())
		{
			add(list, "modifier.defense.all", pKey, defensePhysical());
		}
		else
		{
			add(list, "modifier.defense.physical", pKey, defensePhysical());
			add(list, "modifier.defense.magical", pKey, defenseMagical());
		}
		if(evasionPhysical() == evasionMagical())
		{
			add(list, "modifier.evasion.all", pKey, evasionPhysical());
		}
		else
		{
			add(list, "modifier.evasion.physical", pKey, evasionPhysical());
			add(list, "modifier.evasion.magical", pKey, evasionMagical());
		}
		add(list, "modifier.critprotection", pKey, critProtection());
		abilities().forEach(e -> list.add(new ArgsText("modifier.ability", e.name)));
		return list;
	}

	private static void add(List<? super CharSequence> list, String prefix, String pkey, int value)
	{
		if(value != 0)
			list.add(MultiText.lines(prefix, new ArgsText(pkey, value)));
	}
}