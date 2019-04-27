package system2;

import item.*;
import java.util.*;

public class AttackItem2Slot extends AttackItem2
{
	private List<Class> itemTypes;

	public AttackItem2Slot(List<Class> itemTypes)
	{
		super(0, 0, 0, 0, 0, List.of());
		attackModes = List.of();
		this.itemTypes = itemTypes;
	}

	@Override
	public boolean canContain(Item item)
	{
		return itemTypes.stream().anyMatch(e -> e.isInstance(item));
	}
}