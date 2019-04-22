package system2;

import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import logic.*;

public class System2 implements CombatSystem<Stats2, AttackInfo2, AttackItem2>
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
		if(entity instanceof InvEntity)
		{
			List<int[]> v = ((InvEntity) entity).outputInv().viewItems(false)
					.stream().filter(e -> AttackItem2Slot.INSTANCE.canContain(e.item))
					.map(e -> ((AttackItem2) e.item).getRanges(counter)).collect(Collectors.toList());
			HashSet<Integer> ints2 = new HashSet<>();
			for(int[] ints : v)
			{
				for(int i = 0; i < ints.length; i++)
				{
					ints2.add(ints[i]);
				}
			}
			return ints2.stream().sorted().collect(Collectors.toList());
		}
		return List.of();
	}

	@Override
	public List<AttackInfo2> attackInfo(MainState mainState, XEntity entity, Tile loc, Stats2 stats,
			XEntity entityT, Tile locT, Stats2 statsT)
	{
		int distance = mainState.y2.distance(loc, locT);
		return distanceAttackModes(entity, distance)
				.map(mode -> new AttackInfo2(this, entity, loc, stats, mode,
						entityT, locT, statsT, statsT.getLastUsed(), distance))
				.collect(Collectors.toList());
	}

	public Stream<AttackMode2> distanceAttackModes(XEntity entity, int distance)
	{
		if(entity instanceof InvEntity)
		{
			return ((InvEntity) entity).outputInv().viewItems(false)
					.stream().filter(e -> AttackItem2Slot.INSTANCE.canContain(e.item))
					.flatMap(e -> ((AttackItem2) e.item).attackModes().stream())
					.filter(e -> Arrays.stream(e.getRanges(false)).anyMatch(f -> f == distance));
		}
		return Stream.of();
	}

	@Override
	public void preAttack(AttackInfo2 attackInfo)
	{
		attackInfo.stats.setLastUsed(attackInfo.mode);
	}

	@Override
	public void postAttack(AttackInfo2 attackInfo)
	{

	}
}