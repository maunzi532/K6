package system;

import java.util.*;
import java.util.stream.*;

public interface ModifierProvider4
{
	Map<Stats4, List<Modifier4>> modifiers();

	default List<Modifier4> getModifiers(Stats4 stat)
	{
		return Optional.ofNullable(modifiers().get(stat)).orElse(List.of());
	}

	default int stat(Stats4 stat)
	{
		List<Modifier4> list = getModifiers(stat).stream().sorted(Comparator.comparingInt(e -> e.type().ordinal())).collect(Collectors.toList());
		int value = 0;
		for(Modifier4 modifier : list)
		{
			value = modifier.apply(value);
		}
		return value;
	}
}