package system;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import load.*;

public class EnemyLevelSystem implements ClassAndLevelSystem
{
	private static final List<XModifier> ADDITIONAL_MODIFIERS =
			List.of(new XModifier(XStats.MOVEMENT, XModifierType.ADD, 5),
					new XModifier(XStats.ACCESS_RANGE, XModifierType.ADD, 4)/*,
					new XModifier(XStats.ABILITY_RANGE, XModifierType.ADD, 4)*/);

	private final XClass xClass;
	private final int level;
	private final Map<XStats, List<XModifier>> modifiers;

	public EnemyLevelSystem(XClass xClass, int level)
	{
		this.xClass = xClass;
		this.level = level;
		modifiers = Stream.concat(IntStream.range(0, XClass.NUM_OF_LEVEL_STATS)
				.mapToObj(i -> new XModifier(XStats.values()[i], XModifierType.ADD,
						(xClass.enemyBase()[i] + xClass.enemyIncrease()[i] * level) / xClass.enemyDividers()[i])),
				ADDITIONAL_MODIFIERS.stream())
				.collect(Collectors.groupingBy(XModifier::stat));
	}

	@Override
	public Item visItem()
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
	public Map<XStats, List<XModifier>> modifiers()
	{
		return modifiers;
	}

	public static EnemyLevelSystem load(JrsObject data, WorldSettings worldSettings)
	{
		XClass xClass = worldSettings.getXClass(data.get("Class").asText());
		int level = LoadHelper.asInt(data.get("Level"));
		return new EnemyLevelSystem(xClass, level);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		a1.put("CLSType", "Enemy");
		var a2 = a1.startObjectField("CLS");
		a2.put("Class", worldSettings.saveXClass(xClass));
		a2.put("Level", level);
		a2.end();
	}
}