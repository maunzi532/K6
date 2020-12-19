package logic;

import levelmap.*;
import system.*;

public interface WorldControl
{
	WorldSettings worldSettings();

	LevelMap createLevel();

	void updateTeam(String text, String nextLevel);

	void loadFile(String file);
}