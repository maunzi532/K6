package system4;

import item4.*;

public interface ClassAndLevelSystem extends ModifierProvider4, XSaveableS
{
	XClass4 xClass();

	Item4 visItem();

	int level();

	int exp();

	int maxExp();

	int addExp(int expAmount);
}