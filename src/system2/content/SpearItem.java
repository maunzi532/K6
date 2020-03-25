package system2.content;

import java.util.*;
import system2.*;

public class SpearItem implements AI2Class
{
	private static final int[] RANGES = new int[]{1, 2};
	public static final SpearItem INSTANCE = new SpearItem();

	@Override
	public String imageName()
	{
		return "SpearItem.png";
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
		return List.of(FinesseMode.INSTANCE);
	}
}