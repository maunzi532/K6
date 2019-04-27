package system2.content;

import java.util.*;
import system2.*;

public class SpearItem extends AttackItem2
{
	private SpearItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		this(damage, heavy, slow, accuracy, crit, abilities, new int[]{1, 2}, new int[]{1, 2});
	}

	private SpearItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
		attackModes = List.of(new FinesseMode(this));
	}

	@Override
	public int getAdvType()
	{
		return 2;
	}

	public static SpearItem create(int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>(Arrays.asList(extraAbilities));
		return new SpearItem(damage, heavy, slow, accuracy, crit, abilities);
	}

	public static SpearItem standard()
	{
		return create(11, 10, 0, 90, 0);
	}
}