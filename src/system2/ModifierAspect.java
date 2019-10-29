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
}