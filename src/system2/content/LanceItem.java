package system2.content;

import java.util.*;
import system2.*;

public class LanceItem implements AI2Class
{
	private static final int[] RANGES = new int[]{1, 2};
	private static final int[] COUNTER = new int[]{1, 2, 3, 4};
	public static final LanceItem INSTANCE = new LanceItem();

	@Override
	public String imageName()
	{
		return "LanceItem.png";
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
	public boolean magical()
	{
		return false;
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