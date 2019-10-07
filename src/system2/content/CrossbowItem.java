package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class CrossbowItem extends AttackItem2
{
	private static final Image IMAGE = new Image("CrossbowItem.png");

	private CrossbowItem(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(code, damage, heavy, slow, accuracy, crit, abilities, new int[]{3, 4, 5, 6}, new int[]{3, 4, 5, 6});
	}

	private CrossbowItem(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(code, damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
		attackModes = List.of(new StandardMode(this));
	}

	@Override
	public AdvantageType getAdvType()
	{
		return AdvantageType.CROSSBOW;
	}

	@Override
	public Image image()
	{
		return IMAGE;
	}

	public static CrossbowItem create(int code, int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>(Arrays.asList(extraAbilities));
		return new CrossbowItem(code, damage, heavy, slow, accuracy, crit, abilities);
	}
}