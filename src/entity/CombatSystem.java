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
	int movement(XCharacter entity);

	int dashMovement(XCharacter entity);

	int maxAccessRange(XCharacter entity);

	List<Integer> attackRanges(XCharacter entity, boolean counter);

	default List<PathAttackX> pathAttackInfo(XCharacter entity, Tile loc, List<XCharacter> possibleTargets, PathLocation pl)
	{
		return possibleTargets.stream().flatMap(e -> attackInfo(entity, loc, e, e.location()).stream())
				.map(e -> new PathAttackX(pl, e)).collect(Collectors.toList());
	}

	default List<AttackInfo> attackInfo(XCharacter entity, XCharacter entityT)
	{
		return attackInfo(entity, entity.location(), entityT, entityT.location());
	}

	List<AttackInfo> attackInfo(XCharacter entity, Tile loc, XCharacter entityT, Tile locT);

	void preAttack(AttackInfo attackInfo);

	List<AttackMode3> modesForItem(Stats stats, Item item);

	Optional<Item> equippedItem(Stats stats);

	List<Item> allItems();

	RNGDivider2 supplyDivider(AttackInfo attackInfo);

	double enemyAIScore(List<RNGOutcome> outcomes);

	EnemyAI standardAI();

	AnimTimer createAnimationTimer(RNGDivider2 divider);

	AnimTimer createRegenerationAnimation(XCharacter entity);

	AnimTimer createPostAttackAnimation(AttackInfo aI, RNGOutcome result);

	XCharacter loadCharacterOrStartLoc(TileType y1, JrsObject data, ItemLoader itemLoader, Map<String, JrsObject> characters, Inv storage);

	Stats defaultStats(boolean xh);
}