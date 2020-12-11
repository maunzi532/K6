package xstate;

import levelmap.*;
import gui.*;
import java.util.*;

public interface XStateHolder
{
	void setState(NState state);

	NState getState();

	List<NState> getMenu();

	GUIState getGUI();

	List<VisMark> visMarked();

	boolean showAllEnemyReach();

	void updateLevel(LevelMap newLevel);

	LevelMap levelMap();
}