package system2;

import entity.*;
import entity.analysis.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import item.items.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import logic.*;
import system2.analysis.*;
import system2.content.*;

public class System2 implements CombatSystem<Stats2, AttackInfo2, AttackItem2>
{
	private final Random rng = new Random();

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
					.stream().filter(e -> stats.getItemFilter().canContain(e.item))
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
		return distanceAttackModes(entity, stats, distance)
				.map(mode -> new AttackInfo2(rng, entity, loc, stats, mode,
						entityT, locT, statsT, statsT.getLastUsed(), distance))
				.collect(Collectors.toList());
	}

	public Stream<AttackMode2> distanceAttackModes(XEntity entity, Stats2 stats, int distance)
	{
		if(entity instanceof InvEntity)
		{
			return ((InvEntity) entity).outputInv().viewItems(false)
					.stream().filter(e -> stats.getItemFilter().canContain(e.item))
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
	public void postAttack(AttackInfo2 attackInfo){}

	@Override
	public List<Item> allItems()
	{
		return AttackItems2.INSTANCE.itemList;
	}

	@Override
	public RNGDivider supplyDivider(AttackInfo2 attackInfo)
	{
		return new RNGDivider2(attackInfo);
	}

	@Override
	public double enemyAIScore(List<RNGOutcome> outcomes)
	{
		return new EnemyAI2(outcomes).score();
	}

	@Override
	public XEntity loadEntity(TileType y1, MainState mainState, IntBuffer intBuffer)
	{
		int classCode = intBuffer.get();
		Tile location = y1.create2(intBuffer.get(), intBuffer.get());
		Stats2 stats = new Stats2(intBuffer, this);
		if(classCode > 0)
		{
			Inv inv = new WeightInv(intBuffer, this);
			if(classCode == 1)
				return new XHero(location, mainState, stats, inv);
			else
				return new XEnemy(location, mainState, stats, inv);
		}
		return new XEntity(location, mainState, stats);
	}

	@Override
	public Item loadItem(IntBuffer intBuffer)
	{
		if(intBuffer.get() == 0)
		{
			return Items.values()[intBuffer.get()];
		}
		else
		{
			return AttackItems2.INSTANCE.items[intBuffer.get()];
		}
	}
}