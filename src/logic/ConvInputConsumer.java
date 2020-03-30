package logic;

import geom.tile.*;

public interface ConvInputConsumer
{
	void mousePosition(boolean insideGUI, Tile offsetGUITile, int menuOption, int editorOption, Tile mapTile, boolean moved, boolean drag, XKey key);

	void mouseOutside();

	void dragPosition(Tile startTile, Tile endTile, XKey key, boolean finished);

	void noDrag();

	void handleKey(XKey key);

	void tick();

	void tickPaused();
}