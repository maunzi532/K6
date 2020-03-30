package visual1;

import building.blueprint.*;
import geom.*;
import geom.f1.*;
import item.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.*;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.stage.*;
import levelMap.*;
import logic.*;
import logic.editor.*;
import logic.xstate.*;
import visual1.gui.*;
import visual1.sideinfo.*;

public class MainVisual implements XInputInterface
{
	private static final double BORDER = 0.9;
	private static final double BORDER2 = 0.75;

	private final XGraphics graphics;
	private XKeyMap keyMap;
	private Scheme scheme;
	private TileCamera mapCamera;
	private VisualTile visualTile;
	private TileCamera menuCamera;
	private VisualMenu visualMenu;
	private TileCamera guiCamera;
	private VisualGUI visualGUI;
	private VisualLevelEditor visualLevelEditor;
	private VisualSideInfoFrame visualSideInfoFrame;
	private LevelMap levelMap;
	private LevelEditor levelEditor;
	private XStateHolder stateHolder;
	private ConvInputConsumer convInputConsumer;
	private MainState mainState;
	private int screenshake;
	private boolean paused;

	public MainVisual(XGraphics graphics, XKeyMap keyMap, Scheme scheme,
			TileCamera mapCamera, TileCamera menuCamera, TileCamera guiCamera,
			Function<Double, TileCamera> editorSlotCameraSupplier,
			ItemLoader itemLoader, BlueprintFile blueprintFile, String loadFileMap, String loadFileTeam)
	{
		this.graphics = graphics;
		this.keyMap = keyMap;
		this.scheme = scheme;
		this.mapCamera = mapCamera;
		visualTile = new VisualTile(new VisualXArrow(mapCamera.getDoubleType()), graphics);
		this.menuCamera = menuCamera;
		visualMenu = new VisualMenu(graphics);
		this.guiCamera = guiCamera;
		visualGUI = VisualGUI.forCamera(graphics, guiCamera);
		visualLevelEditor = new VisualLevelEditor(graphics, editorSlotCameraSupplier);
		visualSideInfoFrame = new VisualSideInfoFrame(graphics, false);
		levelMap = new LevelMap(mapCamera.getDoubleType());
		levelEditor = new LevelEditor();
		XStateControl stateControl = new XStateControl(levelMap, levelEditor);
		stateHolder = stateControl;
		convInputConsumer = stateControl;
		mainState = new MainState(levelMap, stateHolder, visualSideInfoFrame, itemLoader, blueprintFile);
		stateControl.setMainState(mainState, new StartTurnState());
		loadLevel(loadFileMap, loadFileTeam, itemLoader, levelMap);
		GraphicsContext gd = graphics.gd();
		gd.setTextAlign(TextAlignment.CENTER);
		gd.setTextBaseline(VPos.CENTER);
		draw();
	}

	public void loadLevel(String loadFileMap, String loadFileTeam, ItemLoader itemLoader, LevelMap levelMap)
	{
		File fileMap;
		File fileTeam;
		if(loadFileMap != null && loadFileTeam != null)
		{
			fileMap = new File(loadFileMap);
			fileTeam = new File(loadFileTeam);
		}
		else
		{
			fileMap = new FileChooser().showOpenDialog(null);
			fileTeam = new FileChooser().showOpenDialog(null);
		}
		if(fileMap != null && fileTeam != null && fileMap.exists() && fileTeam.exists())
		{
			try
			{
				new SavedImport(new String(Files.readAllBytes(fileMap.toPath())), new String(Files.readAllBytes(fileTeam.toPath())))
						.importIntoMap3(levelMap, itemLoader, levelMap.storage().inputInv());
			}catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		/*else
		{
			TileType y1 = mainState.y1;
			new Entity2Builder(mainState.levelMap, mainState.combatSystem).setLocation(y1.create2(2, -1))
					.setStats(new Stats(XClasses.hexerClass(), 0, null))
					.addItem(AttackItems2.standardSpell()).create(false);
			Chapter1.createCharacters(mainState.levelMap, mainState.combatSystem, y1.create2(-2, 1), y1.create2(-2, -1), y1.create2(-4, 1),
					y1.create2(-3, 1), y1.create2(-3, -1), y1.create2(-5, 1));
		}*/
		/*levelMap.addArrow(new ShineArrow(List.of(y2.create2(2, 0), y2.create2(4, 1)), 120, true, null, true));
		levelMap.addArrow(new ShineArrow(List.of(y2.create2(-2, 0), y2.create2(4, -4)), 120, true, null, true));
		levelMap.addArrow(new ShineArrow(List.of(y2.create2(-3, 0)), 120, true, null, true));*/
		/*levelMap.addBuilding2(new ProductionBuilding(y2.create2(-2, -2), buildingBlueprintCache.get("BLUE1")));
		levelMap.addBuilding2(new ProductionBuilding(y2.create2(-3, -3), buildingBlueprintCache.get("GSL1")));
		levelMap.addBuilding(new Transporter(y2.create2(-3, -2), buildingBlueprintCache.get("Transporter1")));*/
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
		return mapCamera.getDoubleType().cast(mapCamera.clickLocation(x, y, screenshake));
	}

	@Override
	public void handleKey(XKey key)
	{
		if(key.hasFunction("Camera Left"))
			mapCamera.setXShift(mapCamera.getXShift() - 3);
		if(key.hasFunction("Camera Up"))
			mapCamera.setYShift(mapCamera.getYShift() - 3);
		if(key.hasFunction("Camera Right"))
			mapCamera.setXShift(mapCamera.getXShift() + 3);
		if(key.hasFunction("Camera Down"))
			mapCamera.setYShift(mapCamera.getYShift() + 3);
		if(!paused)
		{
			if(key.hasFunction("Pause"))
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
			if(key.hasFunction("Pause"))
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
			levelMap.tickArrows();
			convInputConsumer.tick();
		}
		else
		{
			convInputConsumer.tickPaused();
		}
		visualSideInfoFrame.tick();
		screenshake = Math.max(screenshake, levelMap.removeFirstScreenshake());
		if(screenshake > 0)
		{
			screenshake--;
		}
		draw();
	}

	private void draw()
	{
		//graphics.gd().clearRect(0, 0, graphics.xHW() * 2, graphics.yHW() * 2);
		visualTile.draw(mapCamera, levelMap, stateHolder.visMarked(), screenshake, scheme);
		visualSideInfoFrame.draw(scheme);
		if(stateHolder.getState().editMode())
		{
			visualLevelEditor.draw(levelEditor, scheme);
		}
		visualGUI.zoomAndDraw(guiCamera, stateHolder.getGUI(), scheme);
		double bLimit = graphics.yHW() - Math.max(visualSideInfoFrame.takeY2(), visualLevelEditor.takeY(stateHolder.getState().editMode()));
		visualMenu.draw(menuCamera, graphics.yHW() - graphics.scaleHW() * 0.08, bLimit, stateHolder, keyMap, scheme);
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
		gd.fillRect(0, 0, xHW * 2, scale * 2);
		gd.setFill(scheme.color("topbar.text"));
		gd.setFont(new Font(scale));
		gd.setTextAlign(TextAlignment.LEFT);
		gd.fillText(turnText(levelMap.turnCounter()), scale, scale);
		gd.fillText(stateHolder.preferBuildings() ? "BCM" : "CCM", xHW / 2, scale);
		if(paused)
			gd.fillText("Paused", xHW, scale);
		gd.setTextAlign(TextAlignment.RIGHT);
		gd.fillText("P to open pause menu", xHW * 2 - scale, scale);
		gd.setTextAlign(TextAlignment.CENTER);
	}

	public String turnText(int turnCounter)
	{
		if(turnCounter <= 0)
		{
			return "Preparation Phase";
		}
		else
		{
			return "Turn " + turnCounter;
		}
	}
}