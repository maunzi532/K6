package system2;

import java.util.*;

public interface AM2Type extends ModifierAspect
{
	@Override
	default String nameForAbility()
	{
		return "Mode";
	}

	int code();

	default boolean inverseMagical()
	{
		return false;
	}

	default int attackCount()
	{
		return 2;
	}

	String tile();

	default List<String> info()
	{
		List<String> list = new ArrayList<>();
		if(inverseMagical())
			list.add("Magic\nInvert");
		if(attackCount() != 2)
			list.add("Attacks\n" + attackCount());
		list.addAll(detailedInfo(true));
		return list;
	}
}