package system1;

import item.*;

public class AttackItem1Slot extends AttackItem1
{
	public static final AttackItem1Slot INSTANCE = new AttackItem1Slot();

	private AttackItem1Slot()
	{
		super(0, 0, 0, 0);
	}

	@Override
	public boolean canContain(Item item)
	{
		return item instanceof AttackItem1;
	}
}