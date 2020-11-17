package item4;

import java.util.*;

public record ItemList4(List<ItemStack4> items)
{
	public ItemList4()
	{
		this(List.of());
	}
}