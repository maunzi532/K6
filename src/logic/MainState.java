package logic;

import building.blueprint.*;
import building.transport.*;
import entity.*;
import file.*;
import geom.f1.*;
import item.*;
import java.util.*;
import levelMap.*;
import levelMap.importX.*;
import logic.sideinfo.*;
import logic.xstate.*;
import system2.*;
import system2.content.*;

public class MainState
{
	//public static final int CL = 20;
	public final TileType y1;
	public final ItemLoader itemLoader;
	public int turnCounter;
	public int screenshake;
	public boolean preferBuildings;
	public boolean showAllEnemyReach;
	public final LevelMap levelMap;
	public final DoubleInv storage;
	public final List<VisMark> visMarked;
	public final BlueprintCache<BuildingBlueprint> buildingBlueprintCache;
	public XStateHolder stateHolder;
	public final CombatSystem combatSystem;
	public final SideInfoFrame sideInfoFrame;

	public MainState(TileType y1, SideInfoFrame sideInfoFrame)
	{
		this.y1 = y1;
		itemLoader = new ItemLoader2();
		turnCounter = -1;
		levelMap = new LevelMap(y1);
		storage = new Storage();
		visMarked = new ArrayList<>();
		buildingBlueprintCache = new BlueprintCache<>("BuildingBlueprints", e -> BuildingBlueprint.create(e, itemLoader));
		combatSystem = new System2();
		this.sideInfoFrame = sideInfoFrame;
	}

	public String turnText()
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

	public String preferBuildingsText()
	{
		if(preferBuildings)
			return "BCM";
		else
			return "ECM";
	}

	public void initialize(String loadFile, String loadFile2)
	{
		SavedImport savedImport = loadFile != null ? new SavedImport(loadFile, loadFile2) : new SavedImport();
		if(savedImport.hasFile())
		{
			savedImport.importIntoMap3(this);
		}
		else
		{
			//new TestImportSector(y2, 8).generate().importIntoMap(levelMap);
			new Entity2Builder(this).setLocation(y1.create2(2, -1)).setStats(new Stats2(XClasses.hexerClass(), 0, null))
					.addItem(AttackItems2.standardSpell()).create(false);
			Chapter1.createCharacters(this, y1.create2(-2, 1), y1.create2(-2, -1), y1.create2(-4, 1),
					y1.create2(-3, 1), y1.create2(-3, -1), y1.create2(-5, 1));
		}
		/*levelMap.addArrow(new ShineArrow(List.of(y2.create2(2, 0), y2.create2(4, 1)), 120, true, null, true));
		levelMap.addArrow(new ShineArrow(List.of(y2.create2(-2, 0), y2.create2(4, -4)), 120, true, null, true));
		levelMap.addArrow(new ShineArrow(List.of(y2.create2(-3, 0)), 120, true, null, true));*/
		/*levelMap.addBuilding2(new ProductionBuilding(y2.create2(-2, -2), buildingBlueprintCache.get("BLUE1")));
		levelMap.addBuilding2(new ProductionBuilding(y2.create2(-3, -3), buildingBlueprintCache.get("GSL1")));
		levelMap.addBuilding(new Transporter(y2.create2(-3, -2), buildingBlueprintCache.get("Transporter1")));*/
	}

	//TODO Build a real level

	//TODO save storage contents
	//TODO put invlocked characters items into storage
	//TODO class names in classes

	//TODO update all enemy reach only when needed
	//TODO all enemy reach show amount of enemies
	//TODO turn limit
	//TODO enemy reinforcements (starting delay)

	//TODO LevelEditor edit PlayerLevelSystem
	//TODO LevelSystem view
	//TODO CharacterCombatGUI show stat calculation
	//TODO AttackInfoGUI show stat calculation

	//TODO change speed effects
	//TODO levelup essence formula
	//TODO add upgraded classes
	//TODO add class abilities (0/6)
	//TODO add items of upgraded classes

	//TODO Enemy AI types
	//TODO Enemy AI type: Activate
	//TODO Enemy AI type: Stationary
	//TODO complicated levelup system

	//TODO save transporter history
	//TODO Refactor sectors
	//TODO XHero and XEnemy not extending XEntity

	//TODO scale tiles, menubar, gui when resizing
	//TODO Make menu bar not overlap with SideInfo
	//TODO improve camera
	//TODO show pause menu
	//TODO show better SideInfo
	//TODO update images

	//LK char - move/attack
	//RK char - inv/trade
	//LK enemy - view reach
	//RK enemy - inv
	//LK building -- claimed area
	//RK building -- inv
	//LK transporter -- connected buildings
	//RK transporter --

	//enter -- end turn
	//esc -- back to NoneState
}