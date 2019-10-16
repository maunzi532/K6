package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class AxeItem implements AI2Class
{
	private static final Image IMAGE = new Image("AxeItem.png");
	private static final int[] RANGES = new int[]{1};
	private static final int[] COUNTER = new int[]{1, 2};
	public static final AxeItem INSTANCE = new AxeItem();

	/*private AxeItem(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities, new int[]{1}, new int[]{1, 2});
	}

	private AxeItem(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities, ranges, counterR);
		attackModes = List.of(new FinesseMode(this));
	}

	@Override
	public AdvantageType getAdvType()
	{
		return AdvantageType.AXE;
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

	/*public static AxeItem create(int code, int damage, int heavy, int adaptive, AdaptiveType adaptiveType, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>();
		abilities.add(Ability2.MELTING);
		abilities.addAll(Arrays.asList(extraAbilities));
		return new AxeItem(code, damage, heavy, adaptive, adaptiveType, slow, accuracy, crit, abilities);
	}*/
}