package statsystem.content;

import java.util.*;
import statsystem.*;

public final class DaggerItem implements AI2Class
{
	private static final int[] RANGES = {1};
	private static final int[] COUNTER = {1, 2};
	public static final DaggerItem INSTANCE = new DaggerItem();

	@Override
	public String image()
	{
		return "equipment.dagger";
	}

	@Override
	public int adaptive()
	{
		return 5;
	}

	@Override
	public AdaptiveType adaptiveType()
	{
		return AdaptiveType.SKILL;
	}

	@Override
	public AdvantageType advType()
	{
		return AdvantageType.DAGGER;
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
	public List<Ability2> abilities()
	{
		return List.of(Ability2.FAST);
	}

	@Override
	public List<AM2Type> attackModes()
	{
		return List.of(FinesseMode.INSTANCE);
	}
}