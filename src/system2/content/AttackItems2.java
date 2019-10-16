package system2.content;

import item.*;
import java.util.*;
import java.util.stream.*;
import system2.*;

public class AttackItems2
{
	public static final AttackItems2 INSTANCE = new AttackItems2();

	public final AttackItem2[] items;
	public final List<Item> itemListA;
	public final List<Item> itemList;

	public AttackItems2()
	{
		items = new AttackItem2[600];

		items[100] = new AI2Builder(DaggerItem.INSTANCE, 7, 7, 70).crit(10).build();
		items[101] = new AI2Builder(DaggerItem.INSTANCE, 7, 11, 90).crit(30).adaptive(5, AdaptiveType.LUCK).build();
		items[102] = new AI2Builder(DaggerItem.INSTANCE, 11, 10, 70).build();
		//items[100] = DaggerItem.create(100, 8, 10, 5, AdaptiveType.SKILL, 0, 90, 20);
		//items[101] = DaggerItem.create(101, 8, 13, 5, AdaptiveType.SPEED, 0, 100, 30);
		//items[102] = DaggerItem.create(102, 14, 15, 0, 50, 30);

		items[200] = new AI2Builder(SpearItem.INSTANCE, 14, 11, 80).build();
		items[201] = new AI2Builder(SpearItem.INSTANCE, 9, 10, 120).build();
		items[202] = new AI2Builder(SpearItem.INSTANCE, 17, 14, 90).adaptive(5, AdaptiveType.SKILL).build();
		/*items[200] = SpearItem.create(200, 16, 14, 5, AdaptiveType.FINESSE, 0, 80, 0);
		items[201] = SpearItem.create(201, 10, 12, 5, AdaptiveType.FINESSE, 0, 120, 0);
		items[202] = SpearItem.create(202, 14, 11, 5, AdaptiveType.FINESSE, 0, 60, 10);*/

		items[300] = new AI2Builder(AxeItem.INSTANCE, 11, 9, 70).build();
		items[301] = new AI2Builder(AxeItem.INSTANCE, 17, 16, 90).build();
		items[302] = new AI2Builder(AxeItem.INSTANCE, 16, 10, 50).build();
		/*items[300] = AxeItem.create(300, 16, 16, 5, AdaptiveType.SKILL, 0, 70, 0);
		items[301] = AxeItem.create(301, 14, 17, 5, AdaptiveType.SKILL, 0, 80, 20);*/
		//items[302] = AxeItem.create(302, 21, 14, 0, 40, 0);

		items[400] = new AI2Builder(SpellItem.INSTANCE, 11, 17, 90).slow(4).build();
		items[401] = new AI2Builder(SpellItem.INSTANCE, 15, 19, 120).slow(6).build();
		items[402] = new AI2Builder(SpellItem.INSTANCE, 11, 12, 60).slow(2).build();
		/*items[400] = SpellItem.create(400, 11, 17, -20, 4, 90, 0);
		items[401] = SpellItem.create(401, 15, 19, -20, 6, 120, 0);
		items[402] = SpellItem.create(402, 11, 12, -20, 2, 60, 0);*/

		items[500] = new AI2Builder(CrossbowItem.INSTANCE, 8, 12, 70).slow(2).build();
		items[501] = new AI2Builder(CrossbowItem.INSTANCE, 6, 11, 100).slow(3).build();
		items[502] = new AI2Builder(CrossbowItem.INSTANCE, 16, 15, 50).crit(20).slow(6).adaptive(5, AdaptiveType.SKILL).build();
		/*items[500] = CrossbowItem.create(500, 13, 16, 5, AdaptiveType.SKILL, 0, 70, 0);
		items[501] = CrossbowItem.create(501, 9, 14, 5, AdaptiveType.FINESSE, 0, 100, 0);
		items[502] = CrossbowItem.create(502, 12, 15, 5, AdaptiveType.SKILL, 0, 50, 20);*/

		itemListA = Arrays.asList(items);
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