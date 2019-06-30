package system2.content;

import item.*;
import java.util.*;
import java.util.stream.*;
import system2.*;

public class AttackItems2
{
	public static final AttackItems2 INSTANCE = new AttackItems2();

	public final AttackItem2[] items;
	public final List<Item> itemList;

	public AttackItems2()
	{
		items = new AttackItem2[600];

		items[100] = DaggerItem.create(100, 8, 10, 0, 90, 20);
		items[101] = DaggerItem.create(101, 8, 13, 0, 100, 30);
		//items[102] = DaggerItem.create(102, 14, 15, 0, 50, 30);

		items[200] = SpearItem.create(200, 16, 14, 0, 80, 0);
		items[201] = SpearItem.create(201, 10, 12, 0, 120, 0);
		items[202] = SpearItem.create(202, 14, 11, 0, 60, 10);

		items[300] = AxeItem.create(300, 16, 16, 0, 70, 0);
		items[301] = AxeItem.create(301, 14, 17, 0, 80, 20);
		//items[302] = AxeItem.create(302, 21, 14, 0, 40, 0);

		items[400] = SpellItem.create(400, 11, 17, 4, 90, 0);
		items[401] = SpellItem.create(401, 15, 19, 6, 120, 0);
		items[402] = SpellItem.create(402, 11, 12, 2, 60, 0);

		items[500] = CrossbowItem.create(500, 13, 16, 0, 70, 0);
		items[501] = CrossbowItem.create(501, 9, 14, 0, 100, 0);
		items[502] = CrossbowItem.create(502, 12, 15, 0, 50, 20);

		itemList = Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toList());
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