package system2.content;

import java.util.*;
import system2.*;

public class CrossbowItem extends AttackItem2
{
	private CrossbowItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, new int[]{3, 4, 5, 6}, new int[]{3, 4, 5, 6});
	}

	public CrossbowItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
	}

	@Override
	public int getAdvType()
	{
		return 5;
	}

	public static CrossbowItem create(int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>(Arrays.asList(extraAbilities));
		return new CrossbowItem(damage, heavy, slow, accuracy, crit, abilities);
	}

	public static CrossbowItem standard()
	{
		return create(8, 12, 0, 60, 0);
	}
}