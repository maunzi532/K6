package statsystem;

import item.*;
import java.util.*;
import text.*;

public final class AttackItemFilter implements Item
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
		throw new RuntimeException("AttackItemFilter cannot be displayed");
	}

	@Override
	public List<CharSequence> info()
	{
		throw new RuntimeException("AttackItemFilter cannot be displayed");
	}

	@Override
	public int weight()
	{
		throw new RuntimeException("AttackItemFilter cannot be in an inventory");
	}
}