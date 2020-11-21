package system4;

import java.util.*;

public interface ModifierProvider4
{
	Map<Stats4, List<Modifier4>> modifiers();

	default List<Modifier4> getModifiers(Stats4 stat)
	{
		return Optional.ofNullable(modifiers().get(stat)).orElse(List.of());
	}
}