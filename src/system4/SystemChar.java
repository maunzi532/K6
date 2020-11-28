package system4;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import geom.tile.*;
import item4.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;
import load.*;
import text.*;

public final class SystemChar implements XSaveableYS
{
	//Equip inv (TagInv4)
	//Control/AI
	//Stats/Equip/Status (inv/level/status)
	//Exp/Levels/Levelup (PlayerLevelSystem4)
	//HP

	private final ClassAndLevelSystem cls;
	private final TagInv4 inv;
	private final EnemyAI4 enemyAI;
	private final List<ModifierProvider4> modifierProviders;
	private int currentHP;

	public SystemChar(ClassAndLevelSystem cls, TagInv4 inv, EnemyAI4 enemyAI, int currentHP)
	{
		this.cls = cls;
		this.inv = inv;
		this.enemyAI = enemyAI;
		modifierProviders = List.of(cls);
		if(currentHP >= 0)
			this.currentHP = currentHP;
		else
			this.currentHP = stat(Stats4.MAX_HP);
	}

	public int stat(Stats4 stat)
	{
		List<Modifier4> list = modifierProviders.stream().flatMap(e -> e.getModifiers(stat).stream())
				.sorted(Comparator.comparingInt(e -> e.type().ordinal())).collect(Collectors.toList());
		int value = 0;
		for(Modifier4 modifier : list)
		{
			value = modifier.apply(value);
		}
		return value;
	}

	public ClassAndLevelSystem cle()
	{
		return cls;
	}

	public TagInv4 inv()
	{
		return inv;
	}

	public EnemyAI4 enemyAI()
	{
		return enemyAI;
	}

	public int currentHP()
	{
		return currentHP;
	}

	private ModifierProvider4 findEquip(String tag)
	{
		List<Item4> list = inv.taggedItems(tag).stream().filter(e -> e instanceof ModifierProvider4).collect(Collectors.toList());
		if(list.isEmpty())
			return NoEquip.INSTANCE;
		else
			return (ModifierProvider4) list.get(0);
	}

	public ModifierProvider4 equippedCombatItem()
	{
		return findEquip("Defend");
	}

	public CharSequence nameAddedText()
	{
		return new ArgsText("class.withlevel", new LocaleText(cls.visItem().name()), cls.level());
	}

	private Stream<EquipableItem4> allEquipableItems()
	{
		return inv().viewItems().stream()
				.map(NumberedStack4::item)
				.filter(e -> e instanceof EquipableItem4)
				.map(e -> (EquipableItem4) e);
	}

	public List<EquipableItem4> possibleAttackItems()
	{
		return allEquipableItems().filter(e -> e.attackRanges() != null).collect(Collectors.toList());
	}

	public List<EquipableItem4> possibleAllyItems()
	{
		return allEquipableItems().filter(e -> e.allyRanges() != null).collect(Collectors.toList());
	}

	public List<EquipableItem4> possibleDefendItems()
	{
		return allEquipableItems().filter(e -> e.defendRanges() != null).collect(Collectors.toList());
	}

	public List<EquipableItem4> possibleAttackItemsE()
	{
		return allEquipableItems().filter(e -> e.attackRanges() != null && e.tags().contains("Defend")).collect(Collectors.toList());
	}

	public List<EquipableItem4> possibleAttackItems(int distance)
	{
		return possibleAttackItems().stream()
				.filter(e -> e.attackRanges().hasRange(distance, 0))
				.collect(Collectors.toList());
	}

	public EquipableItem4 currentDefendItem(int distance)
	{
		List<EquipableItem4> defendItems1 = inv().taggedItems("Defend").stream()
				.filter(e -> e instanceof EquipableItem4)
				.map(e -> (EquipableItem4) e)
				.filter(e -> e.defendRanges() != null).collect(Collectors.toList());
		List<EquipableItem4> defendItems2 = defendItems1.stream()
				.filter(e -> e.defendRanges().hasRange(distance, 0)).collect(Collectors.toList());
		if(!defendItems2.isEmpty())
			return defendItems2.get(0);
		else if(!defendItems1.isEmpty())
			return defendItems1.get(0);
		else
			return EquipableItem4.NO_EQUIP_ITEM;
	}

	public List<Integer> attackRanges()
	{
		return possibleAttackItems().stream()
				.flatMapToInt(e -> e.attackRanges().ranges().stream().mapToInt(i -> i))
				.distinct().sorted().boxed().collect(Collectors.toList());
	}

	public List<Integer> allyRanges()
	{
		return List.of();
	}

	public List<Integer> defendRanges()
	{
		return List.of();
	}

	public void setCurrentHP(int hp)
	{
		currentHP = hp;
	}

	public List<EquipableItem4> attackOptions(int distance)
	{
		return possibleAttackItemsE().stream()
				.filter(e -> e.attackRanges().hasRange(distance, 0))
				.collect(Collectors.toList());
	}

	public static SystemChar load(JrsObject data, TileType y1, SystemScheme systemScheme)
	{
		ClassAndLevelSystem cls = switch(data.get("CLSType").asText())
				{
					case "Player" -> new PlayerLevelSystem4(); //TODO
					case "Enemy" -> EnemyLevelSystem4.load((JrsObject) data.get("CLS"), systemScheme);
					default -> throw new RuntimeException("Unknown cls type");
				};
		TagInv4 inv = TagInv4.load((JrsObject) data.get("Inv"), systemScheme);
		EnemyAI4 enemyAI = EnemyAI4.load((JrsObject) data.get("EnemyAI"), y1);
		int currentHP;
		JrsValue v1 = data.get("CurrentHP");
		if(v1 instanceof JrsNumber v2)
			currentHP = LoadHelper.asInt(v2);
		else
			currentHP = -1;
		return new SystemChar(cls, inv, enemyAI, currentHP);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, TileType y1, SystemScheme systemScheme) throws IOException
	{
		cls.save(a1, systemScheme);
		XSaveableS.saveObject("Inv", inv, a1, systemScheme);
		XSaveableY.saveObject("EnemyAI", enemyAI, a1, y1);
		a1.put("CurrentHP", currentHP);
	}
}