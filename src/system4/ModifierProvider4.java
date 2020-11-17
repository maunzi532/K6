package system4;

import java.util.*;

public interface ModifierProvider4
{
	Map<String, Set<Modifier4>> modifiers();

	default Set<Modifier4> get(String name)
	{
		return modifiers().get(name);
	}
}