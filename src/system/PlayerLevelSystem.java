package system;

import com.fasterxml.jackson.jr.ob.comp.*;
import item.*;
import java.io.*;
import java.util.*;

public class PlayerLevelSystem implements ClassAndLevelSystem
{
	private XClass xClass;
	private int level;
	private int exp;
	private int[] stats;
	//TODO

	@Override
	public Item visItem()
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
	public int addExp(int expAmount)
	{
		return 0;
	}

	@Override
	public Map<XStats, List<XModifier>> modifiers()
	{
		return null;
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{

	}
}