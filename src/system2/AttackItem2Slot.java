package system2;

import item.*;

public class AttackItem2Slot extends AttackItem2
{
	public static final AttackItem2Slot INSTANCE = new AttackItem2Slot();

	private AttackItem2Slot()
	{
		super(0, 0, 0, 0);
	}

	@Override
	public boolean canContain(Item item)
	{
		return item instanceof AttackItem2;
	}
}