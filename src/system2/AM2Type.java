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

	String tile();

	List<String> info();
}