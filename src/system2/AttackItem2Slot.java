package system2;

import item.*;
import java.util.*;
import javafx.scene.image.*;
import system2.content.*;

public class AttackItem2Slot extends AttackItem2
{
	private List<Class> itemTypes;

	public AttackItem2Slot(List<Class> itemTypes)
	{
		super(0, 0, 0, 0, 0, 0, List.of());
		attackModes = List.of();
		this.itemTypes = itemTypes;
	}

	@Override
	public Image image()
	{
		return null;
	}

	@Override
	public boolean canContain(Item item)
	{
		return itemTypes.stream().anyMatch(e -> e.isInstance(item));
	}

	@Override
	public AdvantageType getAdvType()
	{
		throw new RuntimeException();
	}

	public List<Class> getItemTypes()
	{
		return itemTypes;
	}
}