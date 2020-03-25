package system2.content;

import item.*;
import java.util.*;
import java.util.stream.*;
import system2.*;

public class AttackItems2
{
	public static final AttackItems2 INSTANCE = new AttackItems2();

	public final AttackItem[] items;
	public final List<Item> itemListA;
	public final List<Item> allItemsList;

	public AttackItems2()
	{
		items = new AttackItem[600];

		items[100] = new AI2Builder(DaggerItem.INSTANCE, 7, 7, 70).crit(10).build();
		items[101] = new AI2Builder(DaggerItem.INSTANCE, 7, 11, 90).crit(30).adaptive(5, AdaptiveType.LUCK).build();
		items[102] = new AI2Builder(DaggerItem.INSTANCE, 11, 10, 70).build();

		items[200] = new AI2Builder(SpearItem.INSTANCE, 14, 11, 80).build();
		items[201] = new AI2Builder(SpearItem.INSTANCE, 9, 10, 120).build();
		items[202] = new AI2Builder(SpearItem.INSTANCE, 17, 14, 90).adaptive(5, AdaptiveType.SKILL).build();

		items[300] = new AI2Builder(AxeItem.INSTANCE, 11, 9, 70).build();
		items[301] = new AI2Builder(AxeItem.INSTANCE, 17, 16, 90).build();
		items[302] = new AI2Builder(AxeItem.INSTANCE, 16, 10, 50).build();

		items[400] = new AI2Builder(EnergySpellItem.INSTANCE, 11, 17, 90).slow(4).adaptive(10, AdaptiveType.COST).build();
		items[401] = new AI2Builder(EnergySpellItem.INSTANCE, 15, 19, 120).slow(6).adaptive(10, AdaptiveType.COST).build();
		items[402] = new AI2Builder(EnergySpellItem.INSTANCE, 11, 12, 60).slow(2).adaptive(10, AdaptiveType.COST).build();

		items[500] = new AI2Builder(CrossbowItem.INSTANCE, 8, 12, 70).slow(2).build();
		items[501] = new AI2Builder(CrossbowItem.INSTANCE, 6, 11, 100).slow(3).build();
		items[502] = new AI2Builder(CrossbowItem.INSTANCE, 16, 15, 50).crit(20).slow(6).adaptive(5, AdaptiveType.SKILL).build();

		itemListA = Arrays.asList(items);
		allItemsList = Stream.concat(Arrays.stream(Items.values()), Arrays.stream(items).filter(Objects::nonNull)).collect(Collectors.toList());
	}

	public static AttackItem standardDagger()
	{
		return INSTANCE.items[100];
	}

	public static AttackItem standardSpear()
	{
		return INSTANCE.items[200];
	}

	public static AttackItem standardAxe()
	{
		return INSTANCE.items[300];
	}

	public static AttackItem standardSpell()
	{
		return INSTANCE.items[400];
	}

	public static AttackItem standardCrossbow()
	{
		return INSTANCE.items[500];
	}
}