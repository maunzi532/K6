package entity;

import item.*;
import java.util.*;
import logic.*;

public class Wugu2 implements Wugu1<Stats2, AttackInfo2>
{
	@Override
	public int movement(MainState mainState, XEntity entity, Stats2 stats)
	{
		return stats.getMovement();
	}

	@Override
	public int maxAccessRange(MainState mainState, XEntity entity, Stats2 stats)
	{
		return 4;
	}

	@Override
	public List<Integer> attackRanges(MainState mainState, XEntity entity, Stats2 stats, boolean counter)
	{
		return List.of(1);
	}

	@Override
	public List<AttackInfo2> attackInfo(MainState mainState, XEntity entity, Stats2 stats, XEntity entityT,
			Stats2 statsT)
	{
		return List.of(AttackInfo2.create(entity, stats, Items.BLUE, entityT, statsT, Items.GSL,
				mainState.y2.distance(entity.location(), entityT.location())));
	}
}