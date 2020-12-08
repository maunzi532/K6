package system;

import item.*;

public interface ClassAndLevelSystem extends ModifierProvider4, XSaveableS
{
	Item visItem();

	int level();

	int exp();

	int maxExp();

	int addExp(int expAmount);
}