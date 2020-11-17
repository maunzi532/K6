package system4;

import item4.*;

public interface ClassAndLevelSystem extends ModifierProvider4
{
	Item4 visItem();

	int level();

	int exp();

	int maxExp();
}