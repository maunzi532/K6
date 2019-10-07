package system2.content;

import java.util.*;
import javafx.scene.image.*;
import system2.*;

public class SpellItem extends AttackItem2
{
	private static final Image IMAGE = new Image("SpellItem.png");

	private SpellItem(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(code, damage, heavy, slow, accuracy, crit, abilities, new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6});
	}

	private SpellItem(int code, int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(code, damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
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
	}

	@Override
	public Image image()
	{
		return IMAGE;
	}

	public static SpellItem create(int code, int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>(Arrays.asList(extraAbilities));
		return new SpellItem(code, damage, heavy, slow, accuracy, crit, abilities);
	}
}