package system2;

public class StaticLevelSystem implements LevelSystem
{
	private int[] baseLevelStats;
	private int[] increase;

	@Override
	public int forLevel(int stat, int level)
	{
		return Math.max(0, baseLevelStats[stat] * 100 + increase[stat] * level) / 100;
	}
}