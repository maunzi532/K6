package system2;

import item.*;
import java.util.*;

public class AttackItemFilter implements Item
{
	private final List<AI2Class> itemTypes;

	public AttackItemFilter(List<AI2Class> itemTypes)
	{
		this.itemTypes = itemTypes;
	}

	@Override
	public boolean canContain(Item item)
	{
		if(item instanceof AttackItem attackItem)
			return itemTypes.stream().anyMatch(e -> e == attackItem.itemClass);
		else
			return false;
	}

	public List<AI2Class> getItemTypes()
	{
		return itemTypes;
	}

	@Override
	public String image()
	{
		throw new RuntimeException();
	}

	@Override
	public List<String> info()
	{
		throw new RuntimeException();
	}

	@Override
	public int weight()
	{
		throw new RuntimeException();
	}
}