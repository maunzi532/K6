package visual;

import file.*;
import geom.*;
import geom.f1.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import logic.*;
import logic.editor.*;
import logic.sideinfo.*;
import logic.xstate.*;
import visual.gui.*;
import visual.keybind.*;
import visual.sideinfo.*;

public class MainVisual implements XInputInterface
{
	private static final double BORDER = 0.9;
	private static final double BORDER2 = 0.75;

	private XGraphics graphics;
	private KeybindFile keybindFile;
	private VisualTile visualTile;
	private TileCamera mapCamera;
	private VisualMenu visualMenu;
	private VisualGUI visualGUI;
	private VisualSideInfo visualSideInfo;
	private VisualLevelEditor visualLevelEditor;
	private LevelEditor levelEditor;
	private MainState mainState;
	private ConvInputConsumer convInputConsumer;
	private boolean paused;

	public MainVisual(XGraphics graphics, TileCamera mapCamera, TileCamera menuCamera, TileCamera guiCamera, String loadFile, String loadFile2)
	{
		this.graphics = graphics;
		this.mapCamera = mapCamera;
		keybindFile = new KeybindFile(ImageLoader.loadTextResource("Keybinds"));
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
		visualTile = new VisualTile(y1, new ArrowViewer(mapCamera.getDoubleType()), mainState.levelMap, mainState.visMarked, graphics.gd());
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
			if(!paused)
			{
				convInputConsumer.mousePosition(visualGUI.inside(xMouse, yMouse, mainState.stateHolder.getGUI()),
						visualGUI.offsetClickLocation(xMouse, yMouse), visualMenu.coordinatesToOption(xMouse, yMouse),
						visualLevelEditor.editorClickNum(xMouse, yMouse, levelEditor), targetedTile(xMouse, yMouse), moved, drag, mouseKey);
			}
		}
		else
		{
			if(!paused)
			{
				convInputConsumer.mouseOutside();
			}
		}
	}

	private Tile targetedTile(double x, double y)
	{
		return mapCamera.getDoubleType().cast(mapCamera.clickLocation(x, y, mainState.screenshake));
	}

	@Override
	public void dragPosition(boolean active, double xStart, double yStart,
			double xMoved, double yMoved, int mouseKey, boolean finished)
	{
		if(paused)
			return;
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
		if(paused)
		{
			if(keyCode == KeyCode.P)
				paused = false;
		}
		else
		{
			if(keyCode == KeyCode.P)
				paused = true;
			else
				convInputConsumer.handleKey(keyCode);
		}
	}

	@Override
	public void tick()
	{
		if(paused)
		{
			convInputConsumer.tickPaused();
		}
		else
		{
			mainState.levelMap.tickArrows();
			convInputConsumer.tick();
			visualSideInfo.tick();
		}
		if(mainState.screenshake > 0)
		{
			mainState.screenshake--;
		}
		draw();
	}

	private void draw()
	{
		//graphics.gd().clearRect(0, 0, graphics.xHW() * 2, graphics.yHW() * 2);
		visualTile.draw(mapCamera, mainState.screenshake);
		visualSideInfo.draw();
		visualLevelEditor.draw(levelEditor);
		visualGUI.draw2(mainState.stateHolder.getGUI());
		visualMenu.draw();
		drawInfoText();
	}

	/*private static final String[] CONTROLS_INFO = new String[]
			{
					"arrowkeys/border to move view",
					"TAB to change between choosing buildings/entities first",
					"Q to enter/exit editing mode",
					"ESC to exit menus"
			};*/

	private void drawInfoText()
	{
		graphics.gd().setFill(Color.BLACK);
		graphics.gd().fillRect(0, 0, graphics.xHW() * 2, graphics.yHW() / 10);
		graphics.gd().setFill(Color.WHITE);
		graphics.gd().setFont(new Font(graphics.yHW() / 20));
		graphics.gd().setTextAlign(TextAlignment.LEFT);
		graphics.gd().fillText(mainState.turnText(), graphics.xHW() / 20, graphics.yHW() / 20);
		graphics.gd().fillText(mainState.preferBuildingsText(), graphics.xHW() / 2, graphics.yHW() / 20);
		if(paused)
			graphics.gd().fillText("Paused", graphics.xHW(), graphics.yHW() / 20);
		graphics.gd().setTextAlign(TextAlignment.RIGHT);
		graphics.gd().fillText("P to open pause menu", graphics.xHW() * 2 - graphics.xHW() / 20, graphics.yHW() / 20);
		graphics.gd().setTextAlign(TextAlignment.CENTER);
	}
}