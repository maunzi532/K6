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

		items[200] = new AI2Builder(SpearItem.INSTANCE, 14, 11, 80).build();
		items[201] = new AI2Builder(SpearItem.INSTANCE, 9, 10, 120).build();
		items[202] = new AI2Builder(SpearItem.INSTANCE, 17, 14, 90).adaptive(5, AdaptiveType.SKILL).build();

		items[300] = new AI2Builder(AxeItem.INSTANCE, 11, 9, 70).build();
		items[301] = new AI2Builder(AxeItem.INSTANCE, 17, 16, 90).build();
		items[302] = new AI2Builder(AxeItem.INSTANCE, 16, 10, 50).build();

		items[400] = new AI2Builder(SpellItem.INSTANCE, 11, 17, 90).slow(4).build();
		items[401] = new AI2Builder(SpellItem.INSTANCE, 15, 19, 120).slow(6).build();
		items[402] = new AI2Builder(SpellItem.INSTANCE, 11, 12, 60).slow(2).build();

		items[500] = new AI2Builder(CrossbowItem.INSTANCE, 8, 12, 70).slow(2).build();
		items[501] = new AI2Builder(CrossbowItem.INSTANCE, 6, 11, 100).slow(3).build();
		items[502] = new AI2Builder(CrossbowItem.INSTANCE, 16, 15, 50).crit(20).slow(6).adaptive(5, AdaptiveType.SKILL).build();

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