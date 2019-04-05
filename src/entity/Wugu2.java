package entity;

import item.*;
import java.util.*;
import logic.*;

public class Wugu2 implements Wugu1<Stats2>
{
	@Override
	public int movement(MainState mainState, XEntity entity, Stats2 stats)
	{
		return 4;
	}

	@Override
	public int maxAccessRange(MainState mainState, XEntity entity, Stats2 stats)
	{
		return 4;
	}

	@Override
	public List<Integer> attackRanges(MainState mainState, XEntity entity, Stats2 stats, boolean counter)
	{
		return List.of(1, 2);
	}

	@Override
	public List<AttackInfo> attackInfo(MainState mainState, XEntity entity, Stats2 stats, XEntity entityT,
			Stats2 statsT)
	{
		return List.of(new AttackInfo(entity, Items.BLUE, entityT, Items.GSL, 2,
				"6", "4", "x2", "", "80%", "75%", "crt\n10%", "-"));
	}
}