package system;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import load.*;

public class EnemyLevelSystem4 implements ClassAndLevelSystem
{
	private static final List<Modifier4> ADDITIONAL_MODIFIERS =
			List.of(new Modifier4(Stats4.MOVEMENT, ModifierType4.ADD, 5),
					new Modifier4(Stats4.ACCESS_RANGE, ModifierType4.ADD, 4)/*,
					new Modifier4(Stats4.ABILITY_RANGE, ModifierType4.ADD, 4)*/);

	private final XClass4 xClass;
	private final int level;
	private final Map<Stats4, List<Modifier4>> modifiers;

	public EnemyLevelSystem4(XClass4 xClass, int level)
	{
		this.xClass = xClass;
		this.level = level;
		modifiers = Stream.concat(IntStream.range(0, XClass4.NUM_OF_LEVEL_STATS)
				.mapToObj(i -> new Modifier4(Stats4.values()[i], ModifierType4.ADD,
						(xClass.enemyBase()[i] + xClass.enemyIncrease()[i] * level) / xClass.enemyDividers()[i])),
				ADDITIONAL_MODIFIERS.stream())
				.collect(Collectors.groupingBy(Modifier4::stat));
	}

	@Override
	public XClass4 xClass()
	{
		return xClass;
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
	public int addExp(int expAmount)
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

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{
		a1.put("CLSType", "Enemy");
		var a2 = a1.startObjectField("CLS");
		a2.put("Class", systemScheme.saveXClass(xClass));
		a2.put("Level", level);
		a2.end();
	}
}