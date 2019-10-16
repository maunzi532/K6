package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class CrossbowItem implements AI2Class
{
	private static final Image IMAGE = new Image("CrossbowItem.png");
	private static final int[] RANGES = new int[]{3, 4, 5, 6};
	public static final CrossbowItem INSTANCE = new CrossbowItem();

	/*private CrossbowItem(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities, new int[]{3, 4, 5, 6}, new int[]{3, 4, 5, 6});
	}

	private CrossbowItem(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities, ranges, counterR);
		attackModes = List.of(new StandardMode(this));
	}

	@Override
	public AdvantageType getAdvType()
	{
		return AdvantageType.CROSSBOW;
	}*/

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

	/*public static CrossbowItem create(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>(Arrays.asList(extraAbilities));
		return new CrossbowItem(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities);
	}*/
}