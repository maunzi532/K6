package statsystem;

public interface LevelSystem
{
	int forLevel(int stat, int level);

	int levelCap();

	int[] getLevelup(Stats current);
}