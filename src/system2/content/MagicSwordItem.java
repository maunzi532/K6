package system2.content;

import java.util.*;
import system2.*;

public class MagicSwordItem implements AI2Class
{
	private static final int[] RANGES = new int[]{1};
	private static final int[] COUNTER = new int[]{1, 2};
	public static final MagicSwordItem INSTANCE = new MagicSwordItem();

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
		return List.of(FinesseMode.INSTANCE);
	}
}