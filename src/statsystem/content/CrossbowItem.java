package statsystem.content;

import java.util.*;
import statsystem.*;

public final class CrossbowItem implements AI2Class
{
	private static final int[] RANGES = {3, 4, 5, 6};
	public static final CrossbowItem INSTANCE = new CrossbowItem();

	@Override
	public CharSequence name()
	{
		return "itemclass.crossbow";
	}

	@Override
	public String image()
	{
		return "equipment.crossbow";
	}

	@Override
	public int adaptive()
	{
		return 5;
	}

	@Override
	public AdaptiveType adaptiveType()
	{
		return AdaptiveType.SPEED;
	}

	@Override
	public AdvantageType advType()
	{
		return AdvantageType.CROSSBOW;
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