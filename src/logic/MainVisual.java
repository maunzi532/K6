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
	private static final double BORDER = 0.8;

	private XGraphics graphics;
	private VisualTile visualTile;
	private TileCamera mapCamera;
	private VisualMenu visualMenu;
	private VisualGUI visualGUI;
	private VisualSideInfo visualSideInfo;
	private LevelEditor levelEditor;
	private MainState mainState;

	public MainVisual(XGraphics graphics, boolean hexTiles, boolean hexMenu, boolean hexGUI)
	{
		this.graphics = graphics;
		if(hexTiles)
			mapCamera = new HexCamera(graphics, 1, 1, 44, 44, 0, 0, new HexMatrix(0.5));
		else
			mapCamera = new QuadCamera(graphics, 1, 1, 44, 44, 0, 0);
		DoubleType y2 = mapCamera.getDoubleType();
		mainState = new MainState(y2);
		mainState.initialize();
		mainState.stateControl.start();
		graphics.gd().setImageSmoothing(false);
		graphics.gd().setTextAlign(TextAlignment.CENTER);
		graphics.gd().setTextBaseline(VPos.CENTER);
		visualTile = new VisualTile(y2, new ArrowViewer(y2), mainState.levelMap, graphics.gd());
		visualMenu = new VisualMenu(graphics, mainState.stateControl, hexMenu);
		if(hexGUI)
			visualGUI = new VisualGUIHex(graphics, new HexCamera(graphics, 1, 1, graphics.yHW() / 8, graphics.yHW() / 8, 0, 0, HexMatrix.LP));
		else
			visualGUI = new VisualGUIQuad(graphics, new QuadCamera(graphics, 1, 1, graphics.yHW() / 8, graphics.yHW() / 8, 0, 0));
		visualSideInfo = new VisualSideInfo(graphics);
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
		visualSideInfo.tick();
		draw();
	}

	private void draw()
	{
		//graphics.gd().clearRect(0, 0, graphics.xHW() * 2, graphics.yHW() * 2);
		visualTile.draw(mapCamera);
		visualSideInfo.draw();
		if(mainState.stateControl.getState().editMode())
			levelEditor.draw();
		visualGUI.draw2(mainState.stateControl.getXgui());
		visualMenu.draw();
	}

	public void mousePosition(double xMouse, double yMouse)
	{
		double xp = xMouse / graphics.xHW() - 1;
		double yp = yMouse / graphics.yHW() - 1;
		if(xp > BORDER)
			mapCamera.setXShift(mapCamera.getXShift() + xp - BORDER);
		else if(xp < -BORDER)
			mapCamera.setXShift(mapCamera.getXShift() + xp + BORDER);
		if(yp > BORDER)
			mapCamera.setYShift(mapCamera.getYShift() + yp - BORDER);
		else if(yp < -BORDER)
			mapCamera.setYShift(mapCamera.getYShift() + yp + BORDER);
	}
}