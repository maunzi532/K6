package system2;

import item.*;
import java.util.*;
import javafx.scene.image.*;

public class AttackItem2Slot implements Item
{
	private List<Class> itemTypes;

	public AttackItem2Slot(List<Class> itemTypes)
	{
		this.itemTypes = itemTypes;
	}

	@Override
	public Image image()
	{
		return null; //TODO
	}

	@Override
	public boolean canContain(Item item)
	{
		if(item instanceof AttackItem2)
			return itemTypes.stream().anyMatch(e -> e.isInstance(((AttackItem2) item).itemClass));
		else
			return false;
	}

	@Override
	public List<String> info()
	{
		return List.of(); //TODO
	}

	@Override
	public int weight()
	{
		return 1;
	}

	public List<Class> getItemTypes()
	{
		return itemTypes;
	}
}