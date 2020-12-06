package system;

import item.*;

public interface ClassAndLevelSystem extends ModifierProvider4, XSaveableS
{
	XClass4 xClass();

	Item4 visItem();

	int level();

	int exp();

	int maxExp();

	int addExp(int expAmount);
}