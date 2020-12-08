package logic;

import levelmap.*;
import system.*;

public interface WorldControl
{
	SystemScheme systemScheme();

	LevelMap4 createLevel();

	void updateTeam(String text, String nextLevel);
}