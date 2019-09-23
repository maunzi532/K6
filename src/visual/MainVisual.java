package visual;

import geom.*;
import geom.f1.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import levelMap.editor.*;
import logic.*;
import logic.sideinfo.*;
import logic.xstate.*;
import visual.gui.*;
import visual.sideinfo.*;

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
	private VisualLevelEditor visualLevelEditor;
	private LevelEditor levelEditor;
	private MainState mainState;
	private ConvInputConsumer convInputConsumer;

	public MainVisual(XGraphics graphics, TileCamera mapCamera, TileCamera menuCamera, TileCamera guiCamera, String loadFile, String loadFile2)
	{
		this.graphics = graphics;
		this.mapCamera = mapCamera;
		TileType y1 = mapCamera.getDoubleType();
		SideInfoViewer sivL = new SideInfoViewer(graphics, false);
		SideInfoViewer sivR = new SideInfoViewer(graphics, true);
		visualSideInfo = new VisualSideInfo(sivL, sivR);
		mainState = new MainState(y1, new SideInfoFrame(sivL, sivR));
		mainState.initialize(loadFile, loadFile2);
		graphics.gd().setImageSmoothing(false);
		graphics.gd().setTextAlign(TextAlignment.CENTER);
		graphics.gd().setTextBaseline(VPos.CENTER);
		visualLevelEditor = new VisualLevelEditor(graphics);
		levelEditor = new LevelEditor(mainState);
		convInputConsumer = new StateControl2(mainState, levelEditor, new StartTurnState());
		mainState.stateHolder = (XStateHolder) convInputConsumer;
		visualTile = new VisualTile(y1, new ArrowViewer(mapCamera.getDoubleType()), mainState.levelMap, graphics.gd());
		visualMenu = new VisualMenu(graphics, mainState.stateHolder, menuCamera);
		visualGUI = VisualGUI.forCamera(graphics, guiCamera);
		draw();
	}

	@Override
	public void frameTime(long nanoFrameTime)
	{
		//System.out.println(1000000000L / nanoFrameTime);
	}

	@Override
	public void mousePosition(double xMouse, double yMouse, boolean inside, boolean moved, boolean drag, int mouseKey)
	{
		if(inside)
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
			convInputConsumer.mousePosition(visualGUI.inside(xMouse, yMouse, mainState.stateHolder.getGUI()),
					visualGUI.offsetClickLocation(xMouse, yMouse), visualMenu.coordinatesToOption(xMouse, yMouse),
					visualLevelEditor.editorClickNum(xMouse, yMouse, levelEditor), targetedTile(xMouse, yMouse), moved, drag, mouseKey);
		}
		else
		{
			convInputConsumer.mouseOutside();
		}
	}

	private Tile targetedTile(double x, double y)
	{
		return mapCamera.getDoubleType().cast(mapCamera.clickLocation(x, y));
	}

	@Override
	public void dragPosition(boolean active, double xStart, double yStart,
			double xMoved, double yMoved, int mouseKey, boolean finished)
	{
		if(active)
			convInputConsumer.dragPosition(targetedTile(xStart, yStart), targetedTile(xMoved, yMoved), mouseKey, finished);
		else
			convInputConsumer.noDrag();
	}

	@Override
	public void handleKey(KeyCode keyCode)
	{
		if(keyCode.isArrowKey())
		{
			if(keyCode == KeyCode.RIGHT)
				mapCamera.setXShift(mapCamera.getXShift() + 3);
			if(keyCode == KeyCode.DOWN)
				mapCamera.setYShift(mapCamera.getYShift() + 3);
			if(keyCode == KeyCode.LEFT)
				mapCamera.setXShift(mapCamera.getXShift() - 3);
			if(keyCode == KeyCode.UP)
				mapCamera.setYShift(mapCamera.getYShift() - 3);
		}
		convInputConsumer.handleKey(keyCode);
	}

	@Override
	public void tick()
	{
		mainState.levelMap.tickArrows();
		convInputConsumer.tick();
		visualSideInfo.tick();

		draw();
	}

	private void draw()
	{
		//graphics.gd().clearRect(0, 0, graphics.xHW() * 2, graphics.yHW() * 2);
		visualTile.draw(mapCamera);
		visualSideInfo.draw();
		visualLevelEditor.draw(levelEditor);
		visualGUI.draw2(mainState.stateHolder.getGUI());
		visualMenu.draw();
	}
}