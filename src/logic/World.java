package logic;

import levelmap.*;
import system.*;

public interface World
{
	SystemScheme systemScheme();

	LevelMap4 createLevel();
}