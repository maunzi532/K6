package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class SpellItem implements AI2Class
{
	private static final Image IMAGE = new Image("SpellItem.png");
	private static final int[] RANGES = new int[]{1, 2, 3, 4};
	private static final int[] COUNTER = new int[]{1, 2, 3, 4, 5, 6};
	public static final SpellItem INSTANCE = new SpellItem();

	/*private SpellItem(int code, int damage, int heavy, int adaptive, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(code, damage, heavy, adaptive, slow, accuracy, crit, abilities, new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6});
	}

	private SpellItem(int code, int damage, int heavy, int adaptive, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(code, damage, heavy, adaptive, AdaptiveType.COST, slow, accuracy, crit, abilities, ranges, counterR);
		attackModes = List.of(new StandardMode(this));
	}

	@Override
	public AdvantageType getAdvType()
	{
		return AdvantageType.SPELL;
	}

	@Override
	public boolean magical()
	{
		return true;
	}*/

	@Override
	public Image image()
	{
		return IMAGE;
	}

	@Override
	public int adaptive()
	{
		return 0;
	}

	@Override
	public AdaptiveType adaptiveType()
	{
		return AdaptiveType.COST;
	}

	@Override
	public AdvantageType advType()
	{
		return AdvantageType.SPELL;
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
		return List.of(StandardMode.INSTANCE);
	}

	/*public static SpellItem create(int code, int damage, int heavy, int adaptive, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>(Arrays.asList(extraAbilities));
		return new SpellItem(code, damage, heavy, adaptive, slow, accuracy, crit, abilities);
	}*/
}