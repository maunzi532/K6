package statsystem.content;

import java.util.*;
import statsystem.*;

public final class MagicSwordItem implements AI2Class
{
	private static final int[] RANGES = {1};
	private static final int[] COUNTER = {1, 2};
	public static final MagicSwordItem INSTANCE = new MagicSwordItem();

	@Override
	public CharSequence name()
	{
		return "itemclass.magicsword";
	}

	@Override
	public String image()
	{
		return "equipment.magicsword";
	}

	@Override
	public int adaptive()
	{
		return 10;
	}

	@Override
	public AdaptiveType adaptiveType()
	{
		return AdaptiveType.LUCK;
	}

	@Override
	public AdvantageType advType()
	{
		return AdvantageType.DAGGER;
	}

	@Override
	public DefenseType defenseType()
	{
		return DefenseType.MAGICAL;
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