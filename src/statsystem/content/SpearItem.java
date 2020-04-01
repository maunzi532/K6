package statsystem.content;

import java.util.*;
import statsystem.*;

public final class SpearItem implements AI2Class
{
	private static final int[] RANGES = {1, 2};
	public static final SpearItem INSTANCE = new SpearItem();

	@Override
	public CharSequence name()
	{
		return "itemclass.spear";
	}

	@Override
	public String image()
	{
		return "equipment.spear";
	}

	@Override
	public int adaptive()
	{
		return 5;
	}

	@Override
	public AdaptiveType adaptiveType()
	{
		return AdaptiveType.FINESSE;
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
		return RANGES;
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