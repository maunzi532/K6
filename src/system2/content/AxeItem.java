package system2.content;

import java.util.*;
import system2.*;

public class AxeItem implements AI2Class
{
	private static final int[] RANGES = new int[]{1};
	private static final int[] COUNTER = new int[]{1, 2};
	public static final AxeItem INSTANCE = new AxeItem();

	@Override
	public String imageName()
	{
		return "AxeItem.png";
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
		return List.of(Ability2.MELTING);
	}

	@Override
	public List<AM2Type> attackModes()
	{
		return List.of(FinesseMode.INSTANCE);
	}
}