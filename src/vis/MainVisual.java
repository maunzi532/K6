package vis;

import geom.*;
import geom.tile.*;
import java.util.function.*;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import logic.*;
import logic.editor.*;
import logic.guis.*;
import logic.xstate.*;
import text.*;
import vis.gui.*;
import vis.sideinfo.*;

public final class MainVisual implements XInputInterface
{
	private static final double BORDER = 0.9;
	private static final double BORDER2 = 0.75;

	private final XGraphics graphics;
	private Scheme scheme;
	private TileCamera mapCamera;
	private VisualTile visualTile;
	private TileCamera menuCamera;
	private VisualMenu visualMenu;
	private TileCamera guiCamera;
	private VisualGUI visualGUI;
	private VisualLevelEditor visualLevelEditor;
	private VisualSideInfoFrame visualSideInfoFrame;
	private LevelEditor levelEditor;
	private XStateHolder stateHolder;
	private ConvInputConsumer convInputConsumer;
	private int screenshake;
	private boolean paused;

	public MainVisual(XGraphics graphics, Scheme scheme,
			TileCamera mapCamera, TileCamera menuCamera, TileCamera guiCamera,
			Function<Double, TileCamera> editorSlotCamera, String loadFileTeam)
	{
		this.graphics = graphics;
		this.scheme = scheme;
		this.mapCamera = mapCamera;
		visualTile = new VisualTile(new VisualXArrow(mapCamera.doubleType()), graphics);
		this.menuCamera = menuCamera;
		visualMenu = new VisualMenu(graphics);
		this.guiCamera = guiCamera;
		visualGUI = VisualGUI.forCamera(graphics, guiCamera);
		visualLevelEditor = new VisualLevelEditor(graphics, editorSlotCamera);
		visualSideInfoFrame = new VisualSideInfoFrame(graphics, false);
		WorldLoader worldLoader = new WorldLoader();
		worldLoader.loadFile(loadFileTeam);
		levelEditor = new LevelEditor();
		XStateControl stateControl = new XStateControl(visualSideInfoFrame, levelEditor, worldLoader);
		stateControl.updateLevel(worldLoader.createLevel());
		stateHolder = stateControl;
		convInputConsumer = stateControl;
		stateControl.setState(new LoadLevelGUI());
		//stateControl.setState(new EventListState(levelMap.eventPack("Start").events(), new StartTurnState()));
		GraphicsContext gd = graphics.gd();
		gd.setTextAlign(TextAlignment.CENTER);
		gd.setTextBaseline(VPos.CENTER);
		draw();
	}

	@Override
	public void frameTime(long nanoFrameTime)
	{
		//System.out.println(1000000000L / nanoFrameTime);
	}

	@Override
	public void mousePosition(double xMouse, double yMouse, boolean inside, boolean moved, boolean drag, XKey key)
	{
		if(inside)
		{
			double xp = xMouse / graphics.xHW() - 1.0;
			double yp = yMouse / graphics.yHW() - 1.0;
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
				convInputConsumer.mousePosition(visualGUI.inside(guiCamera, xMouse, yMouse, stateHolder.getGUI()),
						visualGUI.offsetClickLocation(guiCamera, xMouse, yMouse),
						visualMenu.coordinatesToOption(menuCamera, xMouse, yMouse, stateHolder),
						visualLevelEditor.editorClickNum(xMouse, yMouse, levelEditor),
						targetedTile(xMouse, yMouse), moved, drag, key);
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

	@Override
	public void dragPosition(boolean active, double xStart, double yStart,
			double xMoved, double yMoved, XKey key, boolean finished)
	{
		if(!paused)
		{
			if(active)
				convInputConsumer.dragPosition(targetedTile(xStart, yStart), targetedTile(xMoved, yMoved), key, finished);
			else
				convInputConsumer.noDrag();
		}
	}

	private Tile targetedTile(double x, double y)
	{
		return mapCamera.doubleType().cast(mapCamera.clickLocation(x, y, screenshake));
	}

	@Override
	public void handleKey(XKey key)
	{
		if(key.hasFunction("camera.left"))
			mapCamera.setXShift(mapCamera.getXShift() - 3.0);
		if(key.hasFunction("camera.up"))
			mapCamera.setYShift(mapCamera.getYShift() - 3.0);
		if(key.hasFunction("camera.right"))
			mapCamera.setXShift(mapCamera.getXShift() + 3.0);
		if(key.hasFunction("camera.down"))
			mapCamera.setYShift(mapCamera.getYShift() + 3.0);
		if(!paused)
		{
			if(key.hasFunction("pause.toggle"))
			{
				paused = true;
			}
			else
			{
				convInputConsumer.handleKey(key);
			}
		}
		else
		{
			if(key.hasFunction("pause.toggle"))
			{
				paused = false;
			}
		}
	}

	@Override
	public void tick()
	{
		if(!paused)
		{
			stateHolder.levelMap().tickArrows();
			convInputConsumer.tick();
		}
		else
		{
			convInputConsumer.tickPaused();
		}
		visualSideInfoFrame.tick();
		//screenshake = Math.max(screenshake, levelMap.removeFirstScreenshake());
		if(screenshake > 0)
		{
			screenshake--;
		}
		draw();
	}

	private void draw()
	{
		//graphics.gd().clearRect(0, 0, graphics.xHW() * 2, graphics.yHW() * 2);
		visualTile.draw(mapCamera, stateHolder.levelMap(), stateHolder.visMarked(), screenshake, scheme);
		visualSideInfoFrame.draw(scheme);
		if(stateHolder.getState().editMode())
		{
			visualLevelEditor.draw(levelEditor, scheme);
		}
		visualGUI.zoomAndDraw(guiCamera, stateHolder.getGUI(), scheme);
		double bLimit = graphics.yHW() - Math.max(visualSideInfoFrame.takeY2(), visualLevelEditor.takeY(stateHolder.getState().editMode()));
		visualMenu.draw(menuCamera, graphics.yHW() - graphics.scaleHW() * 0.08, bLimit, stateHolder, scheme);
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
		double xHW = graphics.xHW();
		double scale = graphics.scaleHW() * 0.04;
		GraphicsContext gd = graphics.gd();
		gd.setFill(scheme.color("topbar.background"));
		gd.fillRect(0.0, 0.0, xHW * 2.0, scale * 2.0);
		gd.setFill(scheme.color("topbar.text"));
		gd.setFont(new Font(scale));
		gd.setTextAlign(TextAlignment.LEFT);
		gd.fillText(turnText(stateHolder.levelMap().turnCounter()), scale, scale);
		if(paused)
			gd.fillText(scheme.localXText("pause"), xHW, scale);
		gd.setTextAlign(TextAlignment.RIGHT);
		gd.fillText(scheme.localXText(new ArgsText("pause.info", new KeyFunction("pause.toggle"))), xHW * 2.0 - scale, scale);
		gd.setTextAlign(TextAlignment.CENTER);
	}

	private String turnText(int turnCounter)
	{
		if(turnCounter <= 0)
		{
			return scheme.localXText("turn.display.0");
		}
		else
		{
			return scheme.localXText(new ArgsText("turn.display", turnCounter));
		}
	}
}