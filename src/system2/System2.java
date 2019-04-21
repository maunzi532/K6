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
		//Optional<AttackItem2> itemT = counterItem(mainState, entityT, statsT, distance);
		return distanceAttackModes(mainState, entity, stats, distance).stream()
				.map(item -> AttackInfo2.create(this, entity, loc, stats, item, entityT, locT, statsT,
						/*itemT.map(e -> e.attackModes().get(0)).orElse(null)*/statsT.getLastUsed(), distance))
				.collect(Collectors.toList());
	}

	public List<AttackMode2> distanceAttackModes(MainState mainState, XEntity entity, Stats2 stats, int distance)
	{
		if(entity instanceof InvEntity)
		{
			return ((InvEntity) entity).outputInv().viewItems(false)
					.stream().filter(e -> AttackItem2Slot.INSTANCE.canContain(e.item))
					.flatMap(e -> ((AttackItem2) e.item).attackModes().stream())
					.filter(e -> Arrays.stream(e.getRanges(false)).anyMatch(f -> f == distance)).collect(Collectors.toList());
		}
		return List.of();
	}

	public Optional<AttackItem2> counterItem(MainState mainState, XEntity entity, Stats2 stats, int distance)
	{
		if(entity instanceof InvEntity)
		{
			AttackItem2 item = (AttackItem2) ((InvEntity) entity).outputInv().viewRecipeItem(AttackItem2Slot.INSTANCE).item;
			if(Arrays.stream(item.getRanges(true)).anyMatch(e -> e == distance))
				return Optional.of(item);
			else
				return Optional.empty();
		}
		return Optional.empty();
	}

	public int[][] info(XEntity entity, Stats2 stats, AttackMode2 mode,
			XEntity entityT, Stats2 statsT, AttackMode2 modeT, int distance)
	{
		int speed = mode != null ? stats.getSpeed() - mode.getHeavy() : stats.getSpeed();
		int speedT = modeT != null ? statsT.getSpeed() - modeT.getHeavy() : statsT.getSpeed();
		int[][] info = new int[2][4];
		info[0][0] = mode != null ? Math.max(0, stats.getFinesse() + mode.getDamage() - statsT.getDefense()) : 0;
		info[0][1] = mode != null ? (speed - speedT >= 5 ? 2 : 1) : 0;
		info[0][2] = mode != null ? Math.min(100, Math.max(0, (stats.getSkill() - statsT.getSkill()) * 5 + mode.getAccuracy())) : 0;
		info[0][3] = mode != null ? Math.min(100, Math.max(0, (stats.getLuck() + mode.getCrit() - statsT.getLuck()) * 2)) : 0;
		info[1][0] = modeT != null ? Math.max(0, statsT.getFinesse() + modeT.getDamage() - stats.getDefense()) : 0;
		info[1][1] = modeT != null ? (speedT - speed >= 5 ? 2 : 1) : 0;
		info[1][2] = modeT != null ? Math.min(100, Math.max(0, (statsT.getSkill() - stats.getSkill()) * 5 + modeT.getAccuracy())) : 0;
		info[1][3] = modeT != null ? Math.min(100, Math.max(0, (statsT.getLuck() + modeT.getCrit() - stats.getLuck()) * 2)) : 0;
		return info;
	}
}