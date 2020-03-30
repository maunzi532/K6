package statsystem.content;

import java.util.*;
import statsystem.*;

public final class AxeItem implements AI2Class
{
	private static final int[] RANGES = {1};
	private static final int[] COUNTER = {1, 2};
	public static final AxeItem INSTANCE = new AxeItem();

	@Override
	public String image()
	{
		return "equipment.axe";
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
		return AdvantageType.AXE;
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
		return List.of(Ability2.MELTING);
	}

	@Override
	public List<AM2Type> attackModes()
	{
		return List.of(FinesseMode.INSTANCE);
	}
}