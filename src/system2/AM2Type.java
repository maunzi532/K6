package system2;

import java.util.*;

public interface AM2Type extends ModifierAspect
{
	int code();

	default boolean inverseMagical()
	{
		return false;
	}

	default int attackCount()
	{
		return 2;
	}

	@Override
	default boolean p()
	{
		return true;
	}

	@Override
	default List<String> extraText()
	{
		List<String> list = new ArrayList<>();
		if(inverseMagical())
			list.add("Magic\nInvert");
		if(attackCount() != 2)
			list.add("Attacks\n" + attackCount());
		return list;
	}

	String tile();

	List<String> info();
}