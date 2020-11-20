package system4;

import item4.*;
import load.*;

public interface ClassAndLevelSystem extends ModifierProvider4, XSaveable
{
	Item4 visItem();

	int level();

	int exp();

	int maxExp();
}