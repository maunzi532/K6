package system4;

import item4.*;
import java.util.*;

public class EnemyLevelSystem4 implements ClassAndLevelSystem
{
	private final XClass4 xClass;
	private final int level;
	private final Map<String, List<Modifier4>> allModifiers;

	public EnemyLevelSystem4(XClass4 xClass, int level)
	{
		this.xClass = xClass;
		this.level = level;
		allModifiers = Map.of(); //TODO
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
	public Map<String, List<Modifier4>> allModifiers()
	{
		return allModifiers;
	}

	@Override
	public List<Modifier4> getModifiers(String name)
	{
		return allModifiers.get(name);
	}
}