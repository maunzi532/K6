package logic;

import draw.*;
import geom.d1.*;
import geom.hex.HexMatrix;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.text.TextAlignment;

public class MainVisual
{
	private VisualTile visualTile;
	private HexCamera mapCamera;
	private VisualMenu visualMenu;
	private VisualGUI visualGUI;
	private MainState mainState;

	public MainVisual(GraphicsContext gd, int w, int h)
	{
		mainState = new MainState();
		mainState.initialize();
		gd.setTextAlign(TextAlignment.CENTER);
		gd.setTextBaseline(VPos.CENTER);
		visualTile = new VisualTile(mainState.y1, mainState.y2, mainState.levelMap, gd);
		mapCamera = new HexCamera(mainState.y2, w / 2f, h / 2f, 44, 44, 0, 0, new HexMatrix(0.5));
		visualMenu = new VisualMenu(mainState.y2, gd, w / 2f, h / 2f, mainState.stateControl);
		visualGUI = new VisualGUIQuad(new QuadDoubleType(), gd, w / 2f, h / 2f, mainState.stateControl);
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
		else if(mainState.stateControl.getState().hasGUI)
		{
			mainState.stateControl.handleGUIClick(visualGUI.clickLocation(x, y), visualGUI.inside(x, y), mouseKey);
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
		if(mainState.stateControl.getState().hasGUI && visualGUI.inside(x, y))
		{
			mainState.stateControl.target(visualGUI.clickLocation(x, y));
		}
	}

	public void tick()
	{
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