package system4;

import com.fasterxml.jackson.jr.stree.*;
import item4.*;
import java.util.*;
import java.util.stream.*;
import load.*;

public class EnemyLevelSystem4 implements ClassAndLevelSystem
{
	private final XClass4 xClass;
	private final int level;
	private final Map<Stats4, List<Modifier4>> modifiers;

	public EnemyLevelSystem4(XClass4 xClass, int level)
	{
		this.xClass = xClass;
		this.level = level;
		modifiers = IntStream.range(0, XClass4.NUM_OF_LEVEL_STATS)
				.mapToObj(i -> new Modifier4(Stats4.values()[i], ModifierType4.ADD,
						(xClass.enemyBase()[i] + xClass.enemyIncrease()[i] * level) / xClass.enemyDividers()[i]))
				.collect(Collectors.groupingBy(Modifier4::stat));
	}

	@Override
	public Item4 visItem()
	{
		return xClass.visItem();
	}

	@Override
	public int level()
	{
		return level;
	}

	@Override
	public int exp()
	{
		return 0;
	}

	@Override
	public int maxExp()
	{
		return 0;
	}

	@Override
	public Map<Stats4, List<Modifier4>> modifiers()
	{
		return modifiers;
	}

	public static EnemyLevelSystem4 load(JrsObject data, SystemScheme systemScheme)
	{
		XClass4 xClass = systemScheme.getXClass(data.get("Class").asText());
		int level = LoadHelper.asInt(data.get("Level"));
		return new EnemyLevelSystem4(xClass, level);
	}
}