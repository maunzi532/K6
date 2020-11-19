package system4;

import item4.*;
import java.util.*;

public class PlayerLevelSystem4 implements ClassAndLevelSystem
{
	private XClass4 xClass;
	private int level;
	private int exp;
	private int[] stats;
	//TODO

	@Override
	public Item4 visItem()
	{
		return null;
	}

	@Override
	public int level()
	{
		return 0;
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
		return null;
	}
}