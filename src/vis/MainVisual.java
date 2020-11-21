package vis;

import com.fasterxml.jackson.jr.stree.*;
import geom.*;
import geom.tile.*;
import item.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.*;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import levelmap.*;
import load.*;
import logic.*;
import logic.editor.*;
import logic.xstate.*;
import system4.*;
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
	private LevelMap4 levelMap;
	private LevelEditor levelEditor;
	private XStateHolder stateHolder;
	private ConvInputConsumer convInputConsumer;
	private int screenshake;
	private boolean paused;

	public MainVisual(XGraphics graphics, Scheme scheme,
			TileCamera mapCamera, TileCamera menuCamera, TileCamera guiCamera,
			Function<Double, TileCamera> editorSlotCamera,
			ItemLoader itemLoader, String loadFileTeam)
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
		levelMap = loadLevel(loadFileTeam);
		//levelMap = new LevelMap(mapCamera.doubleType());
		levelEditor = new LevelEditor();
		XStateControl stateControl = new XStateControl(levelMap, levelEditor);
		stateHolder = stateControl;
		convInputConsumer = stateControl;
		MainState mainState = new MainState(levelMap, stateHolder, visualSideInfoFrame, itemLoader);
		stateControl.setMainState(mainState, new NoneState());
		//loadLevel(loadFileTeam, itemLoader, levelMap, scheme);
		//stateControl.setState(new EventListState(levelMap.eventPack("Start").events(), new StartTurnState()));
		GraphicsContext gd = graphics.gd();
		gd.setTextAlign(TextAlignment.CENTER);
		gd.setTextBaseline(VPos.CENTER);
		draw();
	}

	private static void loadLevel(String loadFileTeam, ItemLoader itemLoader, LevelMap levelMap, Scheme scheme)
	{
		/*File fileTeam;
		if(loadFileTeam != null)
		{
			fileTeam = new File(loadFileTeam);
		}
		else
		{
			fileTeam = new FileChooser().showOpenDialog(null);
		}
		if(fileTeam != null && fileTeam.exists())
		{
			try
			{
				var dataTeam = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(Files.readString(fileTeam.toPath()));
				if(((JrsNumber) dataTeam.get("code")).getValue().intValue() == 0xA4D2839F)
				{
					String world = ((JrsString) dataTeam.get("World")).getValue();
					String currentMap = ((JrsString) dataTeam.get("CurrentMap")).getValue();
					File levelFile = new File(world, currentMap);
					var dataMap = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(Files.readString(levelFile.toPath()));
					if(((JrsNumber) dataMap.get("code")).getValue().intValue() == 0xA4D2839F)
					{
						levelMap.loadMap((JrsObject) dataMap, itemLoader);
					}
					levelMap.loadTeam((JrsObject) dataTeam, itemLoader);
					levelMap.setEventPacks(EventPack.read(world, currentMap, scheme.setting("file.locale.level"), itemLoader, levelMap.y1));
				}
			}catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}*/
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

	private static LevelMap4 loadLevel(String loadFile)
	{
		try
		{
			Path path1 = Path.of(loadFile);
			JrsObject a1 = LoadHelper.startLoad(path1);
			SystemScheme systemScheme;
			LevelMap4 levelMap;
			JrsValue v1 = a1.get("TurnCounter");
			if(v1 != null)
			{
				Path worldFilePath = Path.of(loadFile, a1.get("WorldFile").asText());
				JrsObject worldFile = LoadHelper.startLoad(worldFilePath);
				systemScheme = SystemScheme.load(worldFile);
				levelMap = LevelMap4.resume(a1, systemScheme);
			}
			else
			{
				Path worldFilePath;
				JrsObject worldFile;
				String levelFile;
				JrsValue v2 = a1.get("WorldFile");
				if(v2 != null)
				{
					//a1 = teamFile
					worldFilePath = Path.of(loadFile, v2.asText());
					worldFile = LoadHelper.startLoad(worldFilePath);
					levelFile = a1.get("CurrentLevel").asText();
				}
				else
				{
					JrsValue v3 = a1.get("StartLevel");
					if(v3 != null)
					{
						//a1 = worldFile
						worldFilePath = path1;
						worldFile = a1;
						levelFile = v3.asText();
					}
					else
					{
						throw new RuntimeException("Not a TeamFile or WorldFile");
					}
				}
				systemScheme = SystemScheme.load(worldFile);
				JrsObject level = LoadHelper.startLoad(worldFilePath.getParent().resolve(levelFile));
				levelMap = LevelMap4.load(level, systemScheme);
				if(v2 != null)
					levelMap.loadTeam(a1, systemScheme);
			}
			//levelMap.setEventPacks(EventPack.read(world, currentMap, scheme.setting("file.locale.level"), itemLoader, levelMap.y1));
			return levelMap;
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
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
			levelMap.tickArrows();
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
		visualTile.draw(mapCamera, levelMap, stateHolder.visMarked(), screenshake, scheme);
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
		gd.fillText(turnText(levelMap.turnCounter()), scale, scale);
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