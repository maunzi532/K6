package entity;

import entity.hero.*;
import item.items.*;
import java.util.*;
import java.util.stream.*;
import logic.*;

public class Wugu2 implements Wugu1<Stats2, AttackInfo2, AttackItem2>
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
		if(entity instanceof XHero)
		{
			List<int[]> v = ((XHero) entity).outputInv().viewItems(false)
					.stream().filter(e -> AttackItem2Slot.INSTANCE.canContain(e.item))
					.map(e -> ((AttackItem2) e.item).getRanges()).collect(Collectors.toList());
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
	public List<AttackInfo2> attackInfo(MainState mainState, XEntity entity, Stats2 stats,
			XEntity entityT, Stats2 statsT)
	{
		int distance = mainState.y2.distance(entity.location(), entityT.location());
		Optional<AttackItem2> itemT = equipItem(mainState, entityT, statsT, distance);
		return items(mainState, entity, stats, distance).stream()
				.map(item -> AttackInfo2.create(this, entity, stats, item, entityT, statsT, itemT.orElse(null), distance))
				.collect(Collectors.toList());
	}

	@Override
	public List<AttackItem2> items(MainState mainState, XEntity entity, Stats2 stats, int distance)
	{
		if(entity instanceof XHero)
		{
			return ((XHero) entity).outputInv().viewItems(false)
					.stream().filter(e -> AttackItem2Slot.INSTANCE.canContain(e.item))
					.map(e -> (AttackItem2) e.item)
					.filter(e -> Arrays.stream(e.getRanges()).anyMatch(f -> f == distance)).collect(Collectors.toList());
		}
		return List.of();
	}

	@Override
	public Optional<AttackItem2> equipItem(MainState mainState, XEntity entity, Stats2 stats, int distance)
	{
		if(entity instanceof XHero)
		{
			AttackItem2 item = (AttackItem2) ((XHero) entity).outputInv().viewRecipeItem(AttackItem2Slot.INSTANCE).item;
			if(Arrays.stream(item.getRanges()).anyMatch(e -> e == distance))
				return Optional.of(item);
			else
				return Optional.empty();
		}
		return Optional.empty();
	}

	public int[][] info(XEntity entity, Stats2 stats, AttackItem2 item,
			XEntity entityT, Stats2 statsT, AttackItem2 itemT, int distance)
	{
		int speed = item != null ? stats.getSpeed() - item.getSlowdown() : stats.getSpeed();
		int speedT = itemT != null ? statsT.getSpeed() - itemT.getSlowdown() : statsT.getSpeed();
		int[][] info = new int[2][4];
		info[0][0] = item != null ? Math.max(0, stats.getAttack() + item.getAttack() - statsT.getDefense()) : 0;
		info[0][1] = item != null ? (speed - speedT >= 5 ? 2 : 1) : 0;
		info[0][2] = item != null ? Math.min(100, Math.max(0, (stats.getAccuracy() - statsT.getAccuracy()) * 5 + item.getAccuracy())) : 0;
		info[0][3] = item != null ? Math.min(100, Math.max(0, (stats.getCrit() + item.getCrit() - statsT.getCrit()) * 2)) : 0;
		info[1][0] = itemT != null ? Math.max(0, statsT.getAttack() + itemT.getAttack() - stats.getDefense()) : 0;
		info[1][1] = itemT != null ? (speedT - speed >= 5 ? 2 : 1) : 0;
		info[1][2] = itemT != null ? Math.min(100, Math.max(0, (statsT.getAccuracy() - stats.getAccuracy()) * 5 + itemT.getAccuracy())) : 0;
		info[1][3] = itemT != null ? Math.min(100, Math.max(0, (statsT.getCrit() + itemT.getCrit() - stats.getCrit()) * 2)) : 0;
		return info;
	}
}