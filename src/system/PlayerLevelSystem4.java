package system;

import com.fasterxml.jackson.jr.ob.comp.*;
import item.*;
import java.io.*;
import java.util.*;

public class PlayerLevelSystem4 implements ClassAndLevelSystem
{
	private XClass4 xClass;
	private int level;
	private int exp;
	private int[] stats;
	//TODO

	@Override
	public XClass4 xClass()
	{
		return null;
	}

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
	public int addExp(int expAmount)
	{
		return 0;
	}

	@Override
	public Map<Stats4, List<Modifier4>> modifiers()
	{
		return null;
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, SystemScheme systemScheme) throws IOException
	{

	}
}