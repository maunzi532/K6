package visual;

import building.blueprint.*;
import entity.sideinfo.*;
import file.*;
import geom.*;
import geom.f1.*;
import item.*;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import levelMap.*;
import logic.*;
import logic.editor.*;
import logic.xstate.*;
import system2.*;
import system2.content.*;
import visual.gui.*;
import visual.sideinfo.*;

public class MainVisual implements XInputInterface
{
	private static final double BORDER = 0.9;
	private static final double BORDER2 = 0.75;

	private XGraphics graphics;
	private XKeyMap keyMap;
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

	public MainVisual(XGraphics graphics, XKeyMap keyMap, TileCamera mapCamera, TileCamera menuCamera, TileCamera guiCamera, String loadFile, String loadFile2)
	{
		this.graphics = graphics;
		this.keyMap = keyMap;
		this.mapCamera = mapCamera;
		TileType y1 = mapCamera.getDoubleType();
		SideInfoViewer sivL = new SideInfoViewer(graphics, false);
		SideInfoViewer sivR = new SideInfoViewer(graphics, true);
		visualSideInfo = new VisualSideInfo(sivL, sivR);
		ItemLoader itemLoader = new ItemLoader2();
		mainState = new MainState(y1, itemLoader, new SideInfoFrame(sivL, sivR), new System2(),
				new BlueprintCache<>("BuildingBlueprints", e -> BuildingBlueprint.create(e, itemLoader)));
		((System2) mainState.combatSystem).setLevelMap(mainState.levelMap);
		loadLevel(loadFile, loadFile2);
		graphics.gd().setImageSmoothing(false);
		graphics.gd().setTextAlign(TextAlignment.CENTER);
		graphics.gd().setTextBaseline(VPos.CENTER);
		visualLevelEditor = new VisualLevelEditor(graphics);
		levelEditor = new LevelEditor(mainState);
		convInputConsumer = new StateControl2(mainState, levelEditor, new StartTurnState());
		mainState.stateHolder = (XStateHolder) convInputConsumer;
		visualTile = new VisualTile(y1, new ArrowViewer(mapCamera.getDoubleType()), mainState.levelMap, mainState.visMarked, graphics.gd());
		visualMenu = new VisualMenu(graphics, mainState.stateHolder, menuCamera, keyMap);
		visualGUI = VisualGUI.forCamera(graphics, guiCamera);
		draw();
	}

	public void loadLevel(String loadFile, String loadFile2)
	{
		SavedImport savedImport = loadFile != null ? new SavedImport(loadFile, loadFile2) : new SavedImport();
		if(savedImport.hasFile())
		{
			savedImport.importIntoMap3(mainState.levelMap, mainState.combatSystem, mainState.itemLoader, mainState.storage.inputInv());
		}
		else
		{
			TileType y1 = mainState.y1;
			new Entity2Builder(mainState.levelMap, mainState.combatSystem).setLocation(y1.create2(2, -1))
					.setStats(new Stats(XClasses.hexerClass(), 0, null))
					.addItem(AttackItems2.standardSpell()).create(false);
			Chapter1.createCharacters(mainState.levelMap, mainState.combatSystem, y1.create2(-2, 1), y1.create2(-2, -1), y1.create2(-4, 1),
					y1.create2(-3, 1), y1.create2(-3, -1), y1.create2(-5, 1));
		}
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
				convInputConsumer.mousePosition(visualGUI.inside(xMouse, yMouse, mainState.stateHolder.getGUI()),
						visualGUI.offsetClickLocation(xMouse, yMouse), visualMenu.coordinatesToOption(xMouse, yMouse),
						visualLevelEditor.editorClickNum(xMouse, yMouse, levelEditor), targetedTile(xMouse, yMouse), moved, drag, key);
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
			double xMoved, double yMoved, XKey key, boolean finished)
	{
		if(paused)
			return;
		if(active)
			convInputConsumer.dragPosition(targetedTile(xStart, yStart), targetedTile(xMoved, yMoved), key, finished);
		else
			convInputConsumer.noDrag();
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
		if(paused)
		{
			if(key.hasFunction("Pause"))
				paused = false;
		}
		else
		{
			if(key.hasFunction("Pause"))
				paused = true;
			else
				convInputConsumer.handleKey(key);
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
		mainState.screenshake = Math.max(mainState.screenshake, mainState.levelMap.removeFirstScreenshake());
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