package logic;

import draw.*;
import geom.*;
import geom.d1.*;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import logic.xstate.*;

public class MainVisual
{
	private DoubleType y2;
	private VisualTile visualTile;
	private TileCamera mapCamera;
	private VisualMenu visualMenu;
	private VisualGUI visualGUI;
	private MainState mainState;

	public MainVisual(GraphicsContext gd, int w, int h)
	{
		//mapCamera = new HexCamera(w / 2f, h / 2f, 44, 44, 0, 0, new HexMatrix(0.5));
		mapCamera = new QuadCamera(w / 2f, h / 2f, 44, 44, 0, 0);
		y2 = mapCamera.getDoubleType();
		mainState = new MainState(y2);
		mainState.initialize();
		gd.setTextAlign(TextAlignment.CENTER);
		gd.setTextBaseline(VPos.CENTER);
		visualTile = new VisualTile(y2, new ArrowViewer(y2), mainState.levelMap, gd);
		visualMenu = new VisualMenu(gd, w / 2f, h / 2f, mainState.stateControl);
		visualGUI = new VisualGUIQuad(gd, w / 2f, h / 2f, mainState.stateControl);
		//visualGUI = new VisualGUIHex(gd, w / 2f, h / 2f, mainState.stateControl);
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
			mainState.stateControl.handleGUIClick(visualGUI.y2.toOffset(visualGUI.clickLocation(x, y)), visualGUI.inside(x, y), mouseKey);
		}
		else
		{
			mainState.stateControl.handleMapClick(mainState.y2.cast(mapCamera.clickLocation(x, y)), mouseKey);
		}
	}

	public void handleKey(KeyCode keyCode)
	{
		if(keyCode.isArrowKey())
		{
			switch(keyCode)
			{
				/*case RIGHT -> mapCamera.xShift += 0.5;
				case LEFT -> mapCamera.xShift -= 0.5;
				case UP -> mapCamera.yShift -= 0.5;
				case DOWN -> mapCamera.yShift += 0.5;*/
				case UP -> mainState.stateControl.handleMenuChoose(-1);
				case DOWN -> mainState.stateControl.handleMenuChoose(1);
			}
		}
	}

	public void handleMouseMove(double x, double y)
	{
		if(mainState.stateControl.getState() instanceof NGUIState && visualGUI.inside(x, y))
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
		visualMenu.draw();
		visualGUI.draw();
	}
}