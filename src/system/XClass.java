package system;

import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.util.*;
import load.*;

public record XClass(Item visItem, int[] bonusIncrease, int[] enemyDividers, int[] enemyBase, int[] enemyIncrease)
{
	public static final int NUM_OF_LEVEL_STATS = 6;

	public static XClass load(JrsObject data, Map<String, ? extends Item> items)
	{
		//TODO load items before
		Item visItem = items.get(data.get("VisItem").asText());
		if(visItem == null)
			throw new RuntimeException("Item named \"" + data.get("VisItem").asText() + "\" missing");
		int[] bonusIncrease = LoadHelper.asIntArray(data.get("BonusIncrease"));
		int[] enemyDividers = LoadHelper.asIntArray(data.get("EnemyDividers"));
		int[] enemyBase = LoadHelper.asIntArray(data.get("EnemyBase"));
		int[] enemyIncrease = LoadHelper.asIntArray(data.get("EnemyIncrease"));
		return new XClass(visItem, bonusIncrease, enemyDividers, enemyBase, enemyIncrease);
	}
}