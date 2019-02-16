package logic;

import draw.*;
import geom.hex.HexMatrix;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.text.TextAlignment;
import levelMap.LevelMap;

public class MainVisual
{
	private VisualTile visualTile;
	private HexCamera mapCamera;
	private VisualMenu visualMenu;
	private VisualGUI visualGUI;
	private LevelMap levelMap;
	private XStateControl stateControl;

	public MainVisual(GraphicsContext gd, int w, int h)
	{
		levelMap = new LevelMap();
		new InitializeMap(levelMap);
		stateControl = new XStateControl(levelMap);
		gd.setTextAlign(TextAlignment.CENTER);
		gd.setTextBaseline(VPos.CENTER);
		visualTile = new VisualTile(levelMap, gd);
		mapCamera = new HexCamera(w / 2f, h / 2f, 40, 40, 0, 0, HexMatrix.LP);
		visualMenu = new VisualMenu(gd, w / 2f, h / 2f, stateControl);
		visualGUI = new VisualGUIQuad(gd, w / 2f, h / 2f, stateControl);
		//visualGUI = new VisualGUIHex(gd, w / 2f, h / 2f, stateControl);
		draw();
	}

	public void handleClick(double x, double y, int mouseKey)
	{
		int menuOption = visualMenu.coordinatesToOption(x, y);
		if(menuOption >= 0)
		{
			stateControl.handleMenuClick(menuOption, mouseKey);
		}
		else if(stateControl.getState().hasGUI)
		{
			stateControl.handleGUIClick(visualGUI.clickLocation(x, y), visualGUI.inside(x, y), mouseKey);
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

	public void handleMouseMove(double x, double y)
	{
		if(stateControl.getState().hasGUI && visualGUI.inside(x, y))
		{
			stateControl.target(visualGUI.clickLocation(x, y));
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