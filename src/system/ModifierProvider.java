package system;

import java.util.*;
import java.util.stream.*;

public interface ModifierProvider
{
	Map<XStats, List<XModifier>> modifiers();

	default List<XModifier> getModifiers(XStats stat)
	{
		return Optional.ofNullable(modifiers().get(stat)).orElse(List.of());
	}

	default int stat(XStats stat)
	{
		List<XModifier> list = getModifiers(stat).stream().sorted(Comparator.comparingInt(e -> e.type().ordinal())).collect(Collectors.toList());
		int value = 0;
		for(XModifier modifier : list)
		{
			value = modifier.apply(value);
		}
		return value;
	}
}