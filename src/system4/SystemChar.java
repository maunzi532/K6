package system4;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item4.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import load.*;
import text.*;

public final class SystemChar implements XSaveableS
{
	//Equip inv (TagInv4)
	//Control/AI
	//Stats/Equip/Status (inv/level/status)
	//Exp/Levels/Levelup (PlayerLevelSystem4)
	//HP

	private final ClassAndLevelSystem cls;
	private final TagInv4 inv;
	private final List<ModifierProvider4> modifierProviders;
	private int currentHP;

	public SystemChar(ClassAndLevelSystem cls, TagInv4 inv, int currentHP)
	{
		this.cls = cls;
		this.inv = inv;
		modifierProviders = List.of(cls);
		if(currentHP >= 0)
			this.currentHP = currentHP;
		else
			this.currentHP = stat(Stats4.MAX_HP);
	}

	public SystemChar()
	{
		cls = new EnemyLevelSystem4(new XClass4(new Item4(){
			@Override
			public CharSequence name()
			{
				return "A";
			}

			@Override
			public String image()
			{
				return "A";
			}

			@Override
			public CharSequence info()
			{
				return "A";
			}

			@Override
			public int stackLimit()
			{
				return 0;
			}
		}, new int[]{0, 0, 0, 0}, new int[]{1, 1, 1, 1},
				new int[]{1, 1, 1, 1}, new int[]{0, 0, 0, 0}), 0);
		inv = new TagInv4(0);
		modifierProviders = List.of(cls);
		currentHP = stat(Stats4.MAX_HP);
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

	public Inv4 inv()
	{
		return inv;
	}

	public int currentHP()
	{
		return currentHP;
	}

	private ModifierProvider4 findEquip(String tag)
	{
		List<Item4> list = inv.lockedItems(tag).stream().filter(e -> e instanceof ModifierProvider4).collect(Collectors.toList());
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

	public List<Integer> attackRanges()
	{
		return List.of();
	}

	public static SystemChar load(JrsObject data, SystemScheme systemScheme)
	{
		ClassAndLevelSystem cls = switch(data.get("CLSType").asText())
				{
					case "Player" -> new PlayerLevelSystem4(); //TODO
					case "Enemy" -> EnemyLevelSystem4.load((JrsObject) data.get("CLS"), systemScheme);
					default -> throw new RuntimeException("Unknown cls type");
				};
		TagInv4 inv = TagInv4.load((JrsObject) data.get("Inv"), systemScheme);
		int currentHP;
		JrsValue v1 = data.get("CurrentHP");
		if(v1 instanceof JrsNumber v2)
			currentHP = LoadHelper.asInt(v2);
		else
			currentHP = -1;
		return new SystemChar(cls, inv, currentHP);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		cls.save(a1, systemScheme);
		XSaveableS.saveObject("Inv", inv, a1, systemScheme);
		a1.put("CurrentHP", currentHP);
	}
}