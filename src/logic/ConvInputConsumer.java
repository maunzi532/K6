package logic;

import geom.f1.*;
import gui.*;
import java.util.*;
import javafx.scene.input.*;
import logic.xstate.*;

public interface ConvInputConsumer
{
	NState getState();

	XGUI getGUI();

	List<NState> getMenu();

	void mousePosition(double xRel, double yRel, boolean insideGUI, Tile offsetGUITile,
			int menuOption, int editorOption, Tile mapTile, boolean moved, boolean drag, int mouseKey);

	void dragPosition(Tile startTile, Tile endTile, int mouseKey, boolean finished);

	void handleKey(KeyCode keyCode);

	void tick();
}