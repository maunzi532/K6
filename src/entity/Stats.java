package entity;

import item.*;
import java.util.*;

public interface Stats
{
	int getStat(int num);

	int getMaxStat(int num);

	void change(int change);

	int getRegenerateChange();

	void regenerating();

	Item getItemFilter();

	boolean removeEntity();

	List<String> info();
}