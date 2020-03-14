package entity;

import com.fasterxml.jackson.jr.stree.*;
import entity.analysis.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.util.*;
import java.util.stream.*;
import system2.*;
import system2.analysis.*;

public interface CombatSystem
{
	int movement(XEntity entity, Stats stats);

	int dashMovement(XEntity entity, Stats stats);

	int maxAccessRange(XEntity entity, Stats stats);

	List<Integer> attackRanges(XEntity entity, Stats stats, boolean counter);

	default List<PathAttackX> pathAttackInfo(XEntity entity, Tile loc, Stats stats, List<XEntity> possibleTargets, PathLocation pl)
	{
		return possibleTargets.stream().flatMap(e -> attackInfo(entity, loc, stats, e, e.location(), e.stats).stream())
				.map(e -> new PathAttackX(pl, e)).collect(Collectors.toList());
	}

	default List<AttackInfo> attackInfo(XEntity entity, Stats stats, XEntity entityT, Stats statsT)
	{
		return attackInfo(entity, entity.location, stats, entityT, entityT.location, statsT);
	}

	List<AttackInfo> attackInfo(XEntity entity, Tile loc, Stats stats, XEntity entityT, Tile locT, Stats statsT);

	void preAttack(AttackInfo attackInfo);

	List<AttackMode3> modesForItem(Stats stats, Item item);

	Optional<Item> equippedItem(Stats stats);

	List<Item> allItems();

	RNGDivider2 supplyDivider(AttackInfo attackInfo);

	double enemyAIScore(List<RNGOutcome> outcomes);

	EnemyAI standardAI();

	AnimTimer createAnimationTimer(RNGDivider2 divider);

	AnimTimer createRegenerationAnimation(XEntity entity);

	AnimTimer createPostAttackAnimation(AttackInfo aI, RNGOutcome result);

	XEntity loadEntity(TileType y1, JrsObject data, ItemLoader itemLoader);

	XEntity loadEntityOrStartLoc(TileType y1, JrsObject data, ItemLoader itemLoader, Map<String, JrsObject> characters, Inv storage);

	Stats defaultStats(boolean xh);
}