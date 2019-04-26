package entity;

public interface Stats
{
	int getStat(int num);

	int getMaxStat(int num);

	void change(int change);

	int getStartTurnChange();

	boolean removeEntity();
}