package entity;

import com.fasterxml.jackson.jr.stree.*;
import entity.analysis.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.util.*;
import java.util.stream.*;

public interface CombatSystem<T extends Stats, A extends AttackInfo<T, ?>>
{
	int movement(XEntity entity, T stats);

	int dashMovement(XEntity entity, T stats);

	int maxAccessRange(XEntity entity, T stats);

	List<Integer> attackRanges(XEntity entity, T stats, boolean counter);

	default List<PathAttackX> pathAttackInfo(XEntity entity, Tile loc, T stats, List<XEntity> possibleTargets, PathLocation pl)
	{
		return possibleTargets.stream().flatMap(e -> attackInfo(entity, loc, stats, e, e.location(), (T) e.stats).stream())
				.map(e -> new PathAttackX(pl, e)).collect(Collectors.toList());
	}

	default List<A> attackInfo(XEntity entity, T stats, XEntity entityT, T statsT)
	{
		return attackInfo(entity, entity.location, stats, entityT, entityT.location, statsT);
	}

	List<A> attackInfo(XEntity entity, Tile loc, T stats, XEntity entityT, Tile locT, T statsT);

	void preAttack(A attackInfo);

	List<XMode> modesForItem(Stats stats, Item item);

	Optional<Item> equippedItem(Stats stats);

	List<Item> allItems();

	RNGDivider supplyDivider(A attackInfo);

	double enemyAIScore(List<RNGOutcome> outcomes);

	EnemyAI standardAI();

	AnimTimer createAnimationTimer(RNGDivider divider);

	AnimTimer createRegenerationAnimation(XEntity entity);

	AnimTimer createPostAttackAnimation(AttackInfo<T, ?> aI, RNGOutcome result);

	XEntity loadEntity(TileType y1, JrsObject data, ItemLoader itemLoader);

	XEntity loadEntityOrStartLoc(TileType y1, JrsObject data, ItemLoader itemLoader, Map<String, JrsObject> characters, Inv storage);

	Stats defaultStats(boolean xh);
}