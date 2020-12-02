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

	public void setCurrentHP(int hp)
	{
		currentHP = hp;
	}

	public CharSequence nameAddedText()
	{
		return new ArgsText("class.withlevel", new LocaleText(cls.visItem().name()), cls.level());
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

	private Stream<EquipableItem4> allEquipableItems()
	{
		return inv().viewItems().stream()
				.map(NumberedStack4::item)
				.filter(e -> e instanceof EquipableItem4)
				.map(e -> (EquipableItem4) e);
	}

	public List<Integer> enemyTargetRanges(boolean attack, boolean ability)
	{
		return allEquipableItems().filter(e -> (attack && e.attackRanges() != null) || (ability && e.abilityRanges() != null))
				.flatMapToInt(e -> e.abilityRanges() != null ? e.abilityRanges().ranges(stat(Stats4.ABILITY_RANGE)) : e.attackRanges().ranges(0))
				.distinct().sorted().boxed().collect(Collectors.toList());
	}

	public List<Integer> allyTargetRanges()
	{
		return allEquipableItems().filter(e -> e.allyRanges() != null)
				.flatMapToInt(e -> e.allyRanges().ranges(stat(Stats4.ABILITY_RANGE)))
				.distinct().sorted().boxed().collect(Collectors.toList());
	}

	public EquipableItem4 defendItem()
	{
		List<Item4> defendItems = inv.taggedItems("Defend");
		if(defendItems.isEmpty())
			return EquipableItem4.NO_EQUIP_ITEM;
		else
			return (EquipableItem4) defendItems.get(0);
	}

	public List<EquipableItem4> possibleAttackItems(int distance, boolean attack, boolean ability)
	{
		return allEquipableItems().filter(e ->
				(attack && e.attackRanges() != null && e.attackRanges().hasRange(distance, 0)) ||
				(ability && e.abilityRanges() != null && e.abilityRanges().hasRange(distance, stat(Stats4.ABILITY_RANGE))))
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