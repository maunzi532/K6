package logic;

import draw.*;
import geom.*;
import geom.d1.*;
import geom.f1.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import levelMap.editor.*;
import logic.xstate.*;

public class MainVisual implements XInputInterface
{
	private static final double BORDER = 0.9;
	private static final double BORDER2 = 0.75;

	private XGraphics graphics;
	private VisualTile visualTile;
	private TileCamera mapCamera;
	private VisualMenu visualMenu;
	private VisualGUI visualGUI;
	private VisualSideInfo visualSideInfo;
	private LevelEditor levelEditor;
	private MainState mainState;
	private ConvInputConsumer convInputConsumer;

	public MainVisual(XGraphics graphics, boolean hexTiles, boolean hexMenu, boolean hexGUI, String loadFile)
	{
		this.graphics = graphics;
		if(hexTiles)
			mapCamera = new HexCamera(graphics, 1, 1, 44, 44, 0, 0, new HexMatrix(0.5));
		else
			mapCamera = new QuadCamera(graphics, 1, 1, 44, 44, 0, 0);
		DoubleType y2 = mapCamera.getDoubleType();
		visualSideInfo = new VisualSideInfo(graphics, mapCamera);
		mainState = new MainState(y2, visualSideInfo);
		mainState.initialize(loadFile);
		graphics.gd().setImageSmoothing(false);
		graphics.gd().setTextAlign(TextAlignment.CENTER);
		graphics.gd().setTextBaseline(VPos.CENTER);
		levelEditor = new LevelEditor(graphics, mainState);
		convInputConsumer = new StateControl2(mainState, levelEditor, new StartTurnState());
		mainState.stateHolder = (XStateHolder) convInputConsumer;
		visualTile = new VisualTile(y2, new ArrowViewer(y2), mainState.levelMap, graphics.gd());
		visualMenu = new VisualMenu(graphics, mainState.stateHolder, hexMenu);
		if(hexGUI)
			visualGUI = new VisualGUIHex(graphics, new HexCamera(graphics, 1, 1, graphics.yHW() / 8, graphics.yHW() / 8, 0, 0, HexMatrix.LP));
		else
			visualGUI = new VisualGUIQuad(graphics, new QuadCamera(graphics, 1, 1, graphics.yHW() / 8, graphics.yHW() / 8, 0, 0));
		draw();
	}

	@Override
	public void frameTime(long nanoFrameTime)
	{

	}

	@Override
	public void mousePosition(double xMouse, double yMouse, boolean moved, boolean drag, int mouseKey)
	{
		double xp = xMouse / graphics.xHW() - 1;
		double yp = yMouse / graphics.yHW() - 1;
		if(xp > BORDER)
			mapCamera.setXShift(mapCamera.getXShift() + xp - BORDER2);
		else if(xp < -BORDER)
			mapCamera.setXShift(mapCamera.getXShift() + xp + BORDER2);
		if(yp > BORDER)
			mapCamera.setYShift(mapCamera.getYShift() + yp - BORDER2);
		else if(yp < -BORDER)
			mapCamera.setYShift(mapCamera.getYShift() + yp + BORDER2);
		convInputConsumer.mousePosition(xMouse / graphics.xHW() - 1, yMouse / graphics.yHW() - 1,
				visualGUI.inside(xMouse, yMouse, mainState.stateHolder.getGUI()), visualGUI.offsetClickLocation(xMouse, yMouse),
				visualMenu.coordinatesToOption(xMouse, yMouse), levelEditor.editorClickNum(xMouse, yMouse),
				targetedTile(xMouse, yMouse), moved, drag, mouseKey);
	}

	private Tile targetedTile(double x, double y)
	{
		return mainState.y2.cast(mapCamera.clickLocation(x, y));
	}

	@Override
	public void dragPosition(double xStart, double yStart, double xMoved, double yMoved, int mouseKey, boolean finished)
	{
		convInputConsumer.dragPosition(targetedTile(xStart, yStart), targetedTile(xMoved, yMoved), mouseKey, finished);
	}

	@Override
	public void handleKey(KeyCode keyCode)
	{
		convInputConsumer.handleKey(keyCode);
	}

	@Override
	public void tick()
	{
		convInputConsumer.tick();
		mainState.levelMap.tickArrows();
		visualSideInfo.tick();
		draw();
	}

	private void draw()
	{
		//graphics.gd().clearRect(0, 0, graphics.xHW() * 2, graphics.yHW() * 2);
		visualTile.draw(mapCamera);
		visualSideInfo.draw();
		levelEditor.draw();
		visualGUI.draw2(mainState.stateHolder.getGUI());
		visualMenu.draw();
	}
}