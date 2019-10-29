package system2;

import java.util.*;

public interface ModifierAspect
{
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

	List<String> extraText();

	boolean p();

	default List<String> detailedInfo()
	{
		List<String> list = new ArrayList<>(extraText());
		abilities().forEach(e -> list.add("Ability\n" + e.name));
		add(list, "Weight", heavy(), p());
		add(list, "Damage", attackPower(), p());
		add(list, "Speed", speedMod(), p());
		add(list, "Accuracy", accuracy(), p());
		add(list, "Crit", crit(), p());
		if(defensePhysical() == defenseMagical())
		{
			add(list, "Def (all)", defensePhysical(), p());
		}
		else
		{
			add(list, "Def (phy)", defensePhysical(), p());
			add(list, "Def (mag)", defenseMagical(), p());
		}
		if(evasionPhysical() == evasionMagical())
		{
			add(list, "Evade (all)", evasionPhysical(), p());
		}
		else
		{
			add(list, "Evade (phy)", evasionPhysical(), p());
			add(list, "Evade (mag)", evasionMagical(), p());
		}
		add(list, "Prevent Crit", critProtection(), p());
		return list;
	}

	private static void add(List list, String prefix, int num, boolean p)
	{
		if(num > 0 && p)
			list.add(prefix + "\n+" + num);
		else if(num != 0)
			list.add(prefix + "\n" + num);
	}
}