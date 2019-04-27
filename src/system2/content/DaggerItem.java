package system2.content;

import java.util.*;
import system2.*;

public class DaggerItem extends AttackItem2
{
	private DaggerItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, new int[]{1}, new int[]{1, 2});
	}

	public DaggerItem(int damage, int heavy, int slow, int accuracy, int crit, List<Ability2> abilities, int[] ranges,
			int[] counterR)
	{
		super(damage, heavy, slow, accuracy, crit, abilities, ranges, counterR);
	}

	@Override
	public int getAdvType()
	{
		return 1;
	}

	public static DaggerItem create(int damage, int heavy, int slow, int accuracy, int crit, Ability2... extraAbilities)
	{
		ArrayList<Ability2> abilities = new ArrayList<>();
		abilities.add(Ability2.FAST);
		abilities.addAll(Arrays.asList(extraAbilities));
		return new DaggerItem(damage, heavy, slow, accuracy, crit, abilities);
	}

	public static DaggerItem standard()
	{
		return create(6, 6, 0, 80, 10);
	}
}