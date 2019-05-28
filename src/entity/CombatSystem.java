package entity;

import entity.analysis.*;
import geom.f1.*;
import item.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import logic.*;

public interface CombatSystem<T extends Stats, A extends AttackInfo, I extends Item>
{
	int movement(MainState mainState, XEntity entity, T stats);

	int maxAccessRange(MainState mainState, XEntity entity, T stats);

	List<Integer> attackRanges(MainState mainState, XEntity entity, T stats, boolean counter);

	default List<A> attackInfo(MainState mainState, XEntity entity, Tile loc, T stats, List<XEntity> possibleTargets)
	{
		return possibleTargets.stream().flatMap(e -> attackInfo(mainState, entity, loc, stats, e, e.location(), (T) e.stats).stream())
				.collect(Collectors.toList());
	}

	default List<A> attackInfo(MainState mainState, XEntity entity, T stats, XEntity entityT, T statsT)
	{
		return attackInfo(mainState, entity, entity.location, stats, entityT, entityT.location, statsT);
	}

	List<A> attackInfo(MainState mainState, XEntity entity, Tile loc, T stats, XEntity entityT, Tile locT, T statsT);

	void preAttack(A attackInfo);

	void postAttack(A attackInfo);

	List<Item> allItems();

	RNGDivider supplyDivider(A attackInfo);

	double enemyAIScore(List<RNGOutcome> outcomes);

	XEntity loadEntity(TileType y1, MainState mainState, IntBuffer intBuffer);

	Item loadItem(IntBuffer intBuffer);
}