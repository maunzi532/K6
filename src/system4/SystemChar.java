package system4;

import item4.*;
import java.util.*;
import java.util.stream.*;

public final class SystemChar
{
	//Equip inv (LockableInv4)
	//Control/AI
	//Stats/Equip/Status (inv/level/status)
	//Exp/Levels/Levelup (PlayerLevelSystem4)
	//HP

	public SystemChar()
	{
		cle = new PlayerLevelSystem4();
		inv = new StackInv4(0);
		modifierProviders = List.of();
		currentHP = stat("MaxHP");
	}

	private final ClassAndLevelSystem cle;
	private final Inv4 inv;
	private final List<ModifierProvider4> modifierProviders;
	private int currentHP;

	public int stat(String name)
	{
		List<Modifier4> list = modifierProviders.stream().flatMap(e -> e.getModifiers(name).stream())
				.sorted(Comparator.comparingInt(e -> e.type().ordinal())).collect(Collectors.toList());
		int value = 0;
		for(Modifier4 modifier : list)
		{
			value = modifier.apply(value);
		}
		return value;
	}
}