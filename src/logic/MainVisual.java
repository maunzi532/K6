package logic;

import draw.*;
import hex.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;

public class MainVisual
{
	private VisualTile visualTile;
	private XCamera mapCamera;
	private VisualMenu visualMenu;
	private XCamera menuCamera;
	private VisualGUI visualGUI;
	private XCamera guiCamera;
	private MainLogic mainLogic;

	public MainVisual(GraphicsContext gd, int w, int h)
	{
		mainLogic = new MainLogic();
		visualTile = new VisualTile(mainLogic.getLevelMap(), gd);
		mapCamera = new XCamera(w / 2f, h / 2f, 40, 40, 0, 0, MatrixH.LP);
		visualMenu = new VisualMenu(gd, w / 2f, h / 2f, mainLogic.getMenu());
		menuCamera = visualMenu.camera;
		visualGUI = new VisualGUI(gd, w / 2f, h / 2f, mainLogic.getMenu());
		guiCamera = visualGUI.camera;
		draw();
	}

	public void handleClick(double x, double y, int mouseKey)
	{
		if(handleGUIClick(x, y, mouseKey))
			return;
		if(handleMenuClick(x, y, mouseKey))
			return;
		handleMapClick(x, y, mouseKey);
	}

	private boolean handleGUIClick(double x, double y, int mouseKey)
	{
		DoubleHex h1 = guiCamera.clickLocation(x, y);
		if(visualGUI.inside(h1))
		{
			mainLogic.handleGUIClick(h1.cast(), mouseKey);
			return true;
		}
		return false;
	}

	private boolean handleMenuClick(double x, double y, int mouseKey)
	{
		int option = visualMenu.hexToOption(menuCamera.clickLocation(x, y).cast());
		if(option >= 0)
		{
			mainLogic.handleMenuClick(option, mouseKey);
			return true;
		}
		return false;
	}

	private void handleMapClick(double x, double y, int mouseKey)
	{
		mainLogic.handleMapClick(mapCamera.clickLocation(x, y).cast(), mouseKey);
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
		mainLogic.tick();
		draw();
	}

	private void draw()
	{
		visualTile.draw(mapCamera);
		visualMenu.draw();
		visualGUI.draw();
	}
}