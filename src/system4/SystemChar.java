package system4;

import java.util.*;
import java.util.stream.*;

public class SystemChar
{
	//Equip inv
	//Control/AI
	//Stats/Equip/Status
	//Exp/Levels/Levelup
	//HP

	private final List<ModifierProvider4> modifierProviders = List.of();

	public int stat(String name)
	{
		List<Modifier4> list = modifierProviders.stream().flatMap(e -> e.get(name).stream())
				.sorted(Comparator.comparingInt(e -> e.type().ordinal())).collect(Collectors.toList());
		int value = 0;
		for(Modifier4 modifier : list)
		{
			value = modifier.apply(value);
		}
		return value;
	}
}