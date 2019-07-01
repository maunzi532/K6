package logic;

import geom.f1.*;
import javafx.scene.input.*;

public interface ConvInputConsumer
{
	void mousePosition(double xRel, double yRel, boolean insideGUI, Tile offsetGUITile,
			int menuOption, int editorOption, Tile mapTile, boolean moved, boolean drag, int mouseKey);

	void dragPosition(Tile startTile, Tile endTile, int mouseKey, boolean finished);

	void handleKey(KeyCode keyCode);

	void tick();
}