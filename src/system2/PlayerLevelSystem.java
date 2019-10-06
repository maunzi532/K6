package system2;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class PlayerLevelSystem implements LevelSystem
{
	private static final Random RANDOM = new Random();
	private static final int STAT_COUNT = 8;

	private int baseLevel;
	private int[] baseLevelStats;
	private int[] baseIncrease;
	private int[] assumedIncrease;
	private int levelCap;

	public PlayerLevelSystem(int baseLevel, int[] baseLevelStats, int[] baseIncrease, int levelCap)
	{
		this.baseLevel = baseLevel;
		this.baseLevelStats = baseLevelStats;
		this.baseIncrease = baseIncrease;
		this.levelCap = levelCap;
		setAssumedIncrease();
	}

	public PlayerLevelSystem(int baseLevel, int[] baseLevelStats, int levelCap)
	{
		this.baseLevel = baseLevel;
		this.baseLevelStats = baseLevelStats;
		baseIncrease = Arrays.stream(baseLevelStats).map(e -> e * 5).toArray();
		this.levelCap = levelCap;
		setAssumedIncrease();
	}

	private void setAssumedIncrease()
	{
		assumedIncrease = Arrays.stream(baseIncrease).map(e -> e * 3 / 2).toArray();
	}

	@Override
	public int forLevel(int stat, int level)
	{
		return Math.max(0, baseLevelStats[stat] * 100 + assumedIncrease[stat] * (level - baseLevel)) / 100;
	}

	@Override
	public int levelCap()
	{
		return levelCap;
	}

	@Override
	public int[] getLevelup(Stats2 current)
	{
		return randomLevelup(levelupPercent(current), RANDOM);
	}

	public int[] levelupPercent(Stats2 current)
	{
		int levelDiff = current.getLevel() - baseLevel;
		int assumedStats = Arrays.stream(assumedIncrease).sum() * levelDiff / 100 + Arrays.stream(baseLevelStats).sum();
		int currentStats = current.getCPower();
		int statsModifier = assumedStats - currentStats;
		int[] percent = new int[STAT_COUNT];
		for(int i = 0; i < STAT_COUNT; i++)
		{
			int assumedStat = assumedIncrease[i] * levelDiff / 100 + baseLevelStats[i];
			int currentStat = current.getStat1(i);
			int statModifier = assumedStat - currentStat;
			percent[i] = Math.max(0, Math.min(100, baseIncrease[i] + statModifier + statsModifier / STAT_COUNT));
		}
		return percent;
	}

	public int[] randomLevelup(int[] percent, Random r)
	{
		int[] levelup = new int[STAT_COUNT];
		for(int i = 0; i < STAT_COUNT; i++)
		{
			levelup[i] = r.nextInt(100) < percent[i] ? 1 : 0;
		}
		int missedChances = IntStream.range(0, STAT_COUNT).map(i -> levelup[i] == 0 ? percent[i] : 0).sum();
		int[] get = IntStream.range(0, STAT_COUNT).filter(i -> levelup[i] == 1).toArray();
		int mc1 = missedChances / 100;
		int amount2 = mc1 + (r.nextInt(100) < missedChances - mc1 * 100 ? 1 : 0);
		for(int i = 0; i < amount2; i++)
		{
			levelup[get[r.nextInt(get.length)]]++;
		}
		return levelup;
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		var a2 = a1.put("BaseLevel", baseLevel).startArrayField("BaseLevelStats");
		for(int i = 0; i < STAT_COUNT; i++)
		{
			a2.add(baseLevelStats[i]);
		}
		var a3 = a2.end().startArrayField("BaseIncrease");
		for(int i = 0; i < STAT_COUNT; i++)
		{
			a3.add(baseIncrease[i]);
		}
		return a3.end().put("LevelCap", levelCap);
	}

	public PlayerLevelSystem(JrsObject data)
	{
		baseLevel = ((JrsNumber) data.get("BaseLevel")).getValue().intValue();
		var array1 = ((JrsArray) data.get("BaseLevelStats"));
		baseLevelStats = new int[STAT_COUNT];
		for(int i = 0; i < STAT_COUNT; i++)
		{
			baseLevelStats[i] = ((JrsNumber) array1.get(i)).getValue().intValue();
		}
		var array2 = ((JrsArray) data.get("BaseIncrease"));
		baseIncrease = new int[STAT_COUNT];
		for(int i = 0; i < STAT_COUNT; i++)
		{
			baseIncrease[i] = ((JrsNumber) array2.get(i)).getValue().intValue();
		}
		setAssumedIncrease();
		levelCap = ((JrsNumber) data.get("LevelCap")).getValue().intValue();
	}
}