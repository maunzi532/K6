package system2.content;

import java.util.*;
import system2.*;

public class DarkSpellItem implements AI2Class
{
	private static final int[] RANGES = new int[]{1, 2, 3, 4};
	private static final int[] COUNTER = new int[]{1, 2, 3, 4, 5, 6};
	public static final DarkSpellItem INSTANCE = new DarkSpellItem();

	@Override
	public String image()
	{
		return "equipment.darkspell";
	}

	@Override
	public int adaptive()
	{
		return 0;
	}

	@Override
	public AdaptiveType adaptiveType()
	{
		return AdaptiveType.COST;
	}

	@Override
	public AdvantageType advType()
	{
		return AdvantageType.SPELL;
	}

	@Override
	public boolean magical()
	{
		return true;
	}

	@Override
	public int[] ranges()
	{
		return RANGES;
	}

	@Override
	public int[] counterR()
	{
		return COUNTER;
	}

	@Override
	public List<Ability2> abilities()
	{
		return List.of();
	}

	@Override
	public List<AM2Type> attackModes()
	{
		return List.of(StandardMode.INSTANCE);
	}
}