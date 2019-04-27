package system2.content;

import java.util.*;
import system2.*;

public class AxeItem extends AttackItem2
{
	private AxeItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, new int[]{1}, new int[]{1, 2});
	}

	public AxeItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
	}

	@Override
	public int getAdvType()
	{
		return 3;
	}

	public static AxeItem create(int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>();
		abilities.add(Ability2.MELTING);
		abilities.addAll(Arrays.asList(extraAbilities));
		return new AxeItem(damage, heavy, slow, accuracy, crit, abilities);
	}

	public static AxeItem standard()
	{
		return create(13, 9, 0, 60, 0);
	}
}