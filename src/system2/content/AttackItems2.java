package system2.content;

import system2.*;

public class AttackItems2
{
	public static final AttackItems2 INSTANCE = new AttackItems2();

	public final AttackItem2[] items;

	public AttackItems2()
	{
		items = new AttackItem2[600];
		items[100] = DaggerItem.create(100, 6, 6, 0, 80, 10);
		items[200] = SpearItem.create(200, 11, 10, 0, 90, 0);
		items[300] = AxeItem.create(300, 11, 9, 0, 60, 0);
		items[400] = SpellItem.create(400, 9, 14, 2, 80, 0);
		items[500] = CrossbowItem.create(500, 8, 12, 0, 80, 0);
	}

	public static AttackItem2 standardDagger()
	{
		return INSTANCE.items[100];
	}

	public static AttackItem2 standardSpear()
	{
		return INSTANCE.items[200];
	}

	public static AttackItem2 standardAxe()
	{
		return INSTANCE.items[300];
	}

	public static AttackItem2 standardSpell()
	{
		return INSTANCE.items[400];
	}

	public static AttackItem2 standardCrossbow()
	{
		return INSTANCE.items[500];
	}
}