package entity;

import arrow.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.analysis.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.util.*;
import java.util.stream.*;
import logic.*;

public interface CombatSystem<T extends Stats, A extends AttackInfo, I extends Item>
{
	int movement(MainState mainState, XEntity entity, T stats);

	int maxAccessRange(MainState mainState, XEntity entity, T stats);

	List<Integer> attackRanges(MainState mainState, XEntity entity, T stats, boolean counter);

	default List<PathAttackX> pathAttackInfo(MainState mainState, XEntity entity, Tile loc, T stats, List<XEntity> possibleTargets, PathLocation pl)
	{
		return possibleTargets.stream().flatMap(e -> attackInfo(mainState, entity, loc, stats, e, e.location(), (T) e.stats).stream())
				.map(e -> new PathAttackX(pl, e)).collect(Collectors.toList());
	}

	default List<A> attackInfo(MainState mainState, XEntity entity, T stats, XEntity entityT, T statsT)
	{
		return attackInfo(mainState, entity, entity.location, stats, entityT, entityT.location, statsT);
	}

	List<A> attackInfo(MainState mainState, XEntity entity, Tile loc, T stats, XEntity entityT, Tile locT, T statsT);

	void preAttack(A attackInfo);

	List<Item> allItems();

	RNGDivider supplyDivider(A attackInfo);

	double enemyAIScore(List<RNGOutcome> outcomes);

	EnemyAI standardAI();

	AnimTimer createAnimationTimer(RNGDivider divider, MainState mainState);

	AnimTimer createRegenerationAnimation(XEntity entity, MainState mainState);

	AnimTimer createPostAttackAnimation(AttackInfo aI, RNGOutcome result, MainState mainState);

	XEntity loadEntity(TileType y1, MainState mainState, JrsObject data, ItemLoader itemLoader);

	XEntity loadEntityOrStartLoc(TileType y1, MainState mainState, JrsObject data, ItemLoader itemLoader, Map<String, JrsObject> characters, Inv storage);

	Stats defaultStats(boolean xh);
}