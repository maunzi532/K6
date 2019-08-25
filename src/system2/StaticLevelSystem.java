package system2;

public class StaticLevelSystem implements LevelSystem
{
	private int baseLevel;
	private int[] baseLevelStats;
	private int[] increase;

	public StaticLevelSystem(int baseLevel, int[] baseLevelStats, int[] increase)
	{
		this.baseLevel = baseLevel;
		this.baseLevelStats = baseLevelStats;
		this.increase = increase;
	}

	@Override
	public int forLevel(int stat, int level)
	{
		return Math.max(0, baseLevelStats[stat] * 100 + increase[stat] * (level - baseLevel)) / 100;
	}
}