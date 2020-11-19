package system4;

import java.util.*;

public class NoEquip implements ModifierProvider4
{
	public static final NoEquip INSTANCE = new NoEquip();

	@Override
	public Map<Stats4, List<Modifier4>> modifiers()
	{
		return Map.of();
	}
}