package statsystem.content;

import java.util.*;
import statsystem.*;

public final class LanceItem implements AI2Class
{
	private static final int[] RANGES = {1, 2};
	private static final int[] COUNTER = {1, 2, 3, 4};
	public static final LanceItem INSTANCE = new LanceItem();

	@Override
	public CharSequence name()
	{
		return "itemclass.lance";
	}

	@Override
	public String image()
	{
		return "equipment.lance";
	}

	@Override
	public int adaptive()
	{
		return 10;
	}

	@Override
	public AdaptiveType adaptiveType()
	{
		return AdaptiveType.SKILL;
	}

	@Override
	public AdvantageType advType()
	{
		return AdvantageType.SPEAR;
	}

	@Override
	public DefenseType defenseType()
	{
		return DefenseType.PHYSICAL;
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
	public List<XAbility> abilities()
	{
		return List.of();
	}

	@Override
	public List<AM2Type> attackModes()
	{
		return List.of(FinesseMode.INSTANCE);
	}
}