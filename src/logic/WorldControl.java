package logic;

import levelmap.*;
import system.*;

public interface WorldControl
{
	WorldSettings systemScheme();

	LevelMap createLevel();

	void updateTeam(String text, String nextLevel);
}