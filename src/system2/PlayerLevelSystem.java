package system2;

public class PlayerLevelSystem implements LevelSystem
{
	private static final int STAT_COUNT = 8;

	private int baseLevel;
	private int[] baseLevelStats;
	private int[] baseIncrease;
	private int[] assumedIncrease;

	@Override
	public int forLevel(int stat, int level)
	{
		return Math.max(0, baseLevelStats[stat] * 100 + assumedIncrease[stat] * (level - baseLevel)) / 100;
	}

	public void randomLevelup(Stats2 current)
	{
		int[] levelup = new int[STAT_COUNT];
		for(int i = 0; i < STAT_COUNT; i++)
		{
			//Assumed Stat
			//Assumed Stats
		}
		//Extra stats
	}
}