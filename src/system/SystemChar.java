package system;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import geom.tile.*;
import item.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;
import load.*;
import text.*;

public final class SystemChar implements XSaveableYS
{
	//Equip inv (TagInv)
	//Control/AI
	//Stats/Equip/Status (inv/level/status)
	//Exp/Levels/Levelup (PlayerLevelSystem)
	//HP

	private final ClassAndLevelSystem cls;
	private final TagInv inv;
	private final EnemyAI enemyAI;
	private final List<ModifierProvider> modifierProviders;
	private int currentHP;

	public SystemChar(ClassAndLevelSystem cls, TagInv inv, EnemyAI enemyAI, int currentHP)
	{
		this.cls = cls;
		this.inv = inv;
		this.enemyAI = enemyAI;
		modifierProviders = List.of(cls);
		if(currentHP >= 0)
			this.currentHP = currentHP;
		else
			this.currentHP = stat(XStats.MAX_HP);
	}

	public ClassAndLevelSystem cls()
	{
		return cls;
	}

	public TagInv inv()
	{
		return inv;
	}

	public EnemyAI enemyAI()
	{
		return enemyAI;
	}

	public int currentHP()
	{
		return currentHP;
	}

	public void setCurrentHP(int hp)
	{
		currentHP = hp;
	}

	public CharSequence nameAddedText()
	{
		return new ArgsText("class.withlevel", new LocaleText(cls.visItem().name()), cls.level());
	}

	public int stat(XStats stat)
	{
		List<XModifier> list = modifierProviders.stream().flatMap(e -> e.getModifiers(stat).stream())
				.sorted(Comparator.comparingInt(e -> e.type().ordinal())).collect(Collectors.toList());
		int value = 0;
		for(XModifier modifier : list)
		{
			value = modifier.apply(value);
		}
		return value;
	}

	private Stream<EquipableItem> allEquipableItems()
	{
		return inv().viewItems().stream()
				.map(NumberedStack::item)
				.filter(e -> e instanceof EquipableItem)
				.map(e -> (EquipableItem) e);
	}

	public List<Integer> enemyTargetRanges(boolean attack, boolean ability)
	{
		return allEquipableItems().filter(e -> (attack && e.attackRanges() != null) || (ability && e.abilityRanges() != null))
				.flatMapToInt(e -> e.abilityRanges() != null ? e.abilityRanges().ranges(stat(XStats.ABILITY_RANGE)) : e.attackRanges().ranges(0))
				.distinct().sorted().boxed().collect(Collectors.toList());
	}

	public List<Integer> allyTargetRanges()
	{
		return allEquipableItems().filter(e -> e.allyRanges() != null)
				.flatMapToInt(e -> e.allyRanges().ranges(stat(XStats.ABILITY_RANGE)))
				.distinct().sorted().boxed().collect(Collectors.toList());
	}

	public EquipableItem defendItem()
	{
		List<Item> defendItems = inv.taggedItems("Defend");
		if(defendItems.isEmpty())
			return EquipableItem.NO_EQUIP_ITEM;
		else
			return (EquipableItem) defendItems.get(0);
	}

	public List<EquipableItem> possibleAttackItems(int distance, boolean attack, boolean ability)
	{
		return allEquipableItems().filter(e ->
				(attack && e.attackRanges() != null && e.attackRanges().hasRange(distance, 0)) ||
				(ability && e.abilityRanges() != null && e.abilityRanges().hasRange(distance, stat(XStats.ABILITY_RANGE))))
				.collect(Collectors.toList());
	}

	public List<EquipableItem> possibleAllyItems(int distance, SystemChar ally)
	{
		return allEquipableItems().filter(e -> e.allyRanges() != null && e.allyRanges().hasRange(distance, stat(XStats.ABILITY_RANGE))
				&& ally.canAllyUseItem(e)).collect(Collectors.toList());
	}

	public boolean canAllyUseItem(EquipableItem item)
	{
		return true; //TODO
	}

	public List<CharSequence> statsInfo()
	{
		//TODO
		return Arrays.stream(XStats.values()).map(stat -> "_" + stat.name().toLowerCase() + "\n" + stat(stat)).collect(Collectors.toList());
	}

	public SystemChar createACopy()
	{
		return new SystemChar(cls, inv, enemyAI, currentHP); //TODO
	}

	public static SystemChar load(JrsObject data, TileType y1, WorldSettings worldSettings)
	{
		ClassAndLevelSystem cls = switch(data.get("CLSType").asText())
				{
					case "Player" -> new PlayerLevelSystem(); //TODO
					case "Enemy" -> EnemyLevelSystem.load((JrsObject) data.get("CLS"), worldSettings);
					default -> throw new RuntimeException("Unknown cls type");
				};
		TagInv inv = TagInv.load((JrsObject) data.get("Inv"), worldSettings);
		EnemyAI enemyAI = EnemyAI.load((JrsObject) data.get("EnemyAI"), y1);
		int currentHP;
		JrsValue v1 = data.get("CurrentHP");
		if(v1 instanceof JrsNumber v2)
			currentHP = LoadHelper.asInt(v2);
		else
			currentHP = -1;
		return new SystemChar(cls, inv, enemyAI, currentHP);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, TileType y1, WorldSettings worldSettings) throws IOException
	{
		cls.save(a1, worldSettings);
		XSaveableS.saveObject("Inv", inv, a1, worldSettings);
		XSaveableY.saveObject("EnemyAI", enemyAI, a1, y1);
		a1.put("CurrentHP", currentHP);
	}
}