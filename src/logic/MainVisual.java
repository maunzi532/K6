package logic;

import draw.*;
import geom.*;
import geom.d1.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import levelMap.editor.*;
import logic.xstate.*;

public class MainVisual
{
	private VisualTile visualTile;
	private TileCamera mapCamera;
	private VisualMenu visualMenu;
	private VisualGUI visualGUI;
	private LevelEditor levelEditor;
	private MainState mainState;

	public MainVisual(XGraphics graphics)
	{
		mapCamera = new HexCamera(graphics, 1, 1, 44, 44, 0, 0, new HexMatrix(0.5));
		//mapCamera = new QuadCamera(graphics, 44, 44, 0, 0);
		DoubleType y2 = mapCamera.getDoubleType();
		mainState = new MainState(y2);
		mainState.initialize();
		mainState.stateControl.start();
		graphics.gd().setTextAlign(TextAlignment.CENTER);
		graphics.gd().setTextBaseline(VPos.CENTER);
		visualTile = new VisualTile(y2, new ArrowViewer(y2), mainState.levelMap, graphics.gd());
		visualMenu = new VisualMenu(graphics, mainState.stateControl);
		visualGUI = new VisualGUIQuad(graphics, new QuadCamera(graphics, 1, 1, graphics.yHW() / 8, graphics.yHW() / 8, 0,  0));
		//visualGUI = new VisualGUIHex(graphics, new HexCamera(graphics, 1, 1, graphics.yHW() / 8, graphics.yHW() / 8, 0,  0, HexMatrix.LP));
		levelEditor = new LevelEditor(graphics, mainState);
		draw();
	}

	public void handleClick(double x, double y, int mouseKey)
	{
		int menuOption = visualMenu.coordinatesToOption(x, y);
		if(menuOption >= 0)
		{
			mainState.stateControl.handleMenuClick(menuOption, mouseKey);
		}
		else if(mainState.stateControl.getState() instanceof NGUIState)
		{
			mainState.stateControl.handleGUIClick(visualGUI.y2.toOffset(visualGUI.clickLocation(x, y)),
					visualGUI.inside(x, y, mainState.stateControl.getXgui()), mouseKey);
		}
		else if(mainState.stateControl.getState().editMode())
		{
			mainState.stateControl.handleEditMode(levelEditor, x, y, mainState.y2.cast(mapCamera.clickLocation(x, y)), mouseKey);
		}
		else
		{
			mainState.stateControl.handleMapClick(mainState.y2.cast(mapCamera.clickLocation(x, y)), mouseKey);
		}
	}

	public void handleKey(KeyCode keyCode)
	{
		switch(keyCode)
		{
			/*case RIGHT -> mapCamera.xShift += 0.5;
			case LEFT -> mapCamera.xShift -= 0.5;
			case UP -> mapCamera.yShift -= 0.5;
			case DOWN -> mapCamera.yShift += 0.5;*/
			case UP -> mainState.stateControl.handleMenuChoose(-1);
			case DOWN -> mainState.stateControl.handleMenuChoose(1);
			case E -> mainState.stateControl.toggleEditMode();
		}
	}

	public void handleMouseMove(double x, double y)
	{
		if(mainState.stateControl.getState() instanceof NGUIState && visualGUI.inside(x, y, mainState.stateControl.getXgui()))
		{
			mainState.stateControl.target(visualGUI.y2.toOffset(visualGUI.clickLocation(x, y)));
		}
	}

	public void tick()
	{
		mainState.stateControl.tick();
		mainState.levelMap.tickArrows();
		draw();
	}

	private void draw()
	{
		visualTile.draw(mapCamera);
		if(mainState.stateControl.getState().editMode())
			levelEditor.draw();
		visualGUI.draw(mainState.stateControl.getXgui());
		visualMenu.draw();
	}
}