package entity;

public interface Stats
{
	int getStat(int num);

	int getMaxStat(int num);

	void change(boolean increase);

	boolean removeEntity();
}