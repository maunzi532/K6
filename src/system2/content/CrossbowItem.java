package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class CrossbowItem implements AI2Class
{
	private static final Image IMAGE = new Image("CrossbowItem.png");
	private static final int[] RANGES = new int[]{3, 4, 5, 6};
	public static final CrossbowItem INSTANCE = new CrossbowItem();

	@Override
	public Image image()
	{
		return IMAGE;
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
		return List.of(StandardMode.INSTANCE);
	}
}