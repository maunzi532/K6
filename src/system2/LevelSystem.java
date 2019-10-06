package system2;

public interface LevelSystem
{
	int forLevel(int stat, int level);

	int levelCap();

	int[] getLevelup(Stats2 current);
}