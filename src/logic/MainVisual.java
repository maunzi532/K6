package logic;

import draw.*;
import hex.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import levelMap.LevelMap;

public class MainVisual
{
	private VisualTile visualTile;
	private XCamera mapCamera;
	private VisualMenu visualMenu;
	private XCamera menuCamera;
	private VisualGUI visualGUI;
	private XCamera guiCamera;
	private LevelMap levelMap;
	private XStateControl stateControl;

	public MainVisual(GraphicsContext gd, int w, int h)
	{
		levelMap = new LevelMap();
		new InitializeMap(levelMap);
		stateControl = new XStateControl(levelMap);
		visualTile = new VisualTile(levelMap, gd);
		mapCamera = new XCamera(w / 2f, h / 2f, 40, 40, 0, 0, MatrixH.LP);
		visualMenu = new VisualMenu(gd, w / 2f, h / 2f, stateControl);
		menuCamera = visualMenu.camera;
		visualGUI = new VisualGUI(gd, w / 2f, h / 2f, stateControl);
		guiCamera = visualGUI.camera;
		draw();
	}

	public void handleClick(double x, double y, int mouseKey)
	{
		int menuOption = visualMenu.hexToOption(menuCamera.clickLocation(x, y).cast());
		if(menuOption >= 0)
		{
			stateControl.handleMenuClick(menuOption, mouseKey);
		}
		else if(stateControl.getState().hasGUI)
		{
			DoubleHex h1 = guiCamera.clickLocation(x, y);
			stateControl.handleGUIClick(h1.cast(), visualGUI.inside(h1), mouseKey);
		}
		else
		{
			stateControl.handleMapClick(mapCamera.clickLocation(x, y).cast(), mouseKey);
		}
	}

	public void handleKey(KeyCode keyCode)
	{
		if(keyCode.isArrowKey())
		{
			switch(keyCode)
			{
				case RIGHT -> mapCamera.xShift += 0.5;
				case LEFT -> mapCamera.xShift -= 0.5;
				case UP -> mapCamera.yShift -= 0.5;
				case DOWN -> mapCamera.yShift += 0.5;
			}
		}
	}

	public void tick()
	{
		levelMap.tickArrows();
		draw();
	}

	private void draw()
	{
		visualTile.draw(mapCamera);
		visualMenu.draw();
		visualGUI.draw();
	}
}