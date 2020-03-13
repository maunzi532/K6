package system2;

import item.*;
import java.util.*;
import javafx.scene.image.*;

public class AttackItem2Slot implements Item
{
	private final List<AI2Class> itemTypes;

	public AttackItem2Slot(List<AI2Class> itemTypes)
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
			return itemTypes.stream().anyMatch(e -> e == ((AttackItem2) item).itemClass); //TODO check
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

	public List<AI2Class> getItemTypes()
	{
		return itemTypes;
	}
}