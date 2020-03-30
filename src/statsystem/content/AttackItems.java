package statsystem.content;

import item.*;
import java.util.*;
import java.util.stream.*;
import statsystem.*;

public final class AttackItems
{
	public static final AttackItems INSTANCE = new AttackItems();

	public final AttackItem[] items;
	public final List<Item> itemListA;
	public final List<Item> allItemsList;

	public AttackItems()
	{
		items = new AttackItem[600];

		items[100] = new AIBuilder(DaggerItem.INSTANCE, 7, 7, 70).setCrit(10).build();
		items[101] = new AIBuilder(DaggerItem.INSTANCE, 7, 11, 90).setCrit(30).setAdaptive(5, AdaptiveType.LUCK).build();
		items[102] = new AIBuilder(DaggerItem.INSTANCE, 11, 10, 70).build();

		items[200] = new AIBuilder(SpearItem.INSTANCE, 14, 11, 80).build();
		items[201] = new AIBuilder(SpearItem.INSTANCE, 9, 10, 120).build();
		items[202] = new AIBuilder(SpearItem.INSTANCE, 17, 14, 90).setAdaptive(5, AdaptiveType.SKILL).build();

		items[300] = new AIBuilder(AxeItem.INSTANCE, 11, 9, 70).build();
		items[301] = new AIBuilder(AxeItem.INSTANCE, 17, 16, 90).build();
		items[302] = new AIBuilder(AxeItem.INSTANCE, 16, 10, 50).build();

		items[400] = new AIBuilder(EnergySpellItem.INSTANCE, 11, 17, 90).setSlow(4).setAdaptive(10, AdaptiveType.COST).build();
		items[401] = new AIBuilder(EnergySpellItem.INSTANCE, 15, 19, 120).setSlow(6).setAdaptive(10, AdaptiveType.COST).build();
		items[402] = new AIBuilder(EnergySpellItem.INSTANCE, 11, 12, 60).setSlow(2).setAdaptive(10, AdaptiveType.COST).build();

		items[500] = new AIBuilder(CrossbowItem.INSTANCE, 8, 12, 70).setSlow(2).build();
		items[501] = new AIBuilder(CrossbowItem.INSTANCE, 6, 11, 100).setSlow(3).build();
		items[502] = new AIBuilder(CrossbowItem.INSTANCE, 16, 15, 50).setCrit(20).setSlow(6).setAdaptive(5, AdaptiveType.SKILL).build();

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