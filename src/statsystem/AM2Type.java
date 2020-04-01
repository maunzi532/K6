package statsystem;

import java.util.*;
import text.*;

public interface AM2Type extends ModifierAspect
{
	@Override
	default CharSequence nameForAbility()
	{
		return "modifier.name.mode";
	}

	int code();

	default boolean inverseDefenseType()
	{
		return false;
	}

	default int attackCount()
	{
		return 2;
	}

	CharSequence tile();

	default List<? extends CharSequence> info()
	{
		List<CharSequence> list = new ArrayList<>();
		if(inverseDefenseType())
			list.add("modifier.invertdefense");
		if(attackCount() != 2)
			list.add(new ArgsText("modifier.attackcount", attackCount()));
		list.addAll(detailedInfo(true));
		return list;
	}
}