package entity;

import item.*;

public interface Stats
{
	int getStat(int num);

	int getMaxStat(int num);

	void change(int change);

	int getRegenerateChange();

	void regenerating();

	Item getItemFilter();

	boolean removeEntity();
}