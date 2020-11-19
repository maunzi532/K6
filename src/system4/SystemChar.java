package system4;

import item4.*;
import java.util.*;
import java.util.stream.*;
import text.*;

public final class SystemChar
{
	//Equip inv (LockableInv4)
	//Control/AI
	//Stats/Equip/Status (inv/level/status)
	//Exp/Levels/Levelup (PlayerLevelSystem4)
	//HP

	public SystemChar()
	{
		cle = new EnemyLevelSystem4(new XClass4(null, new int[]{0, 0, 0, 0}, new int[]{1, 1, 1, 1}, new int[]{1, 1, 1, 1}, new int[]{0, 0, 0, 0}), 0);
		inv = new LockableInv4(0);
		modifierProviders = List.of(cle);
		currentHP = stat(Stats4.MAX_HP);
	}

	private final ClassAndLevelSystem cle;
	private final LockableInv4 inv;
	private final List<ModifierProvider4> modifierProviders;
	private int currentHP;

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
		return cle;
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
		return new ArgsText("class.withlevel", new LocaleText(cle.visItem().name()), cle.level());
	}

	public List<Integer> attackRanges()
	{
		return null;
	}
}