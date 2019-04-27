package system2.content;

import java.util.*;
import system2.*;

public class SpellItem extends AttackItem2
{
	private SpellItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6});
	}

	public SpellItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
	}

	@Override
	public int getAdvType()
	{
		return 4;
	}

	@Override
	public boolean magical()
	{
		return true;
	}

	public static SpellItem create(int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>(Arrays.asList(extraAbilities));
		return new SpellItem(damage, heavy, slow, accuracy, crit, abilities);
	}

	public static SpellItem standard()
	{
		return create(9, 14, 2, 80, 0);
	}
}